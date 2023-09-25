package TheShadowMod.patches;

import TheShadowMod.helpers.ShionMaskHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class FlipCardEffectPatches {
    private static float target_x = 0.0f;
    private static float target_y = 0.0f;
    private static float current_x = 0.0f;
    private static float current_y = 0.0f;

    private static float angle = 0.0f;
    private static float targetAngle = 0.0f;

    public static float cardScaleX = 1.0f;

    public static FrameBuffer cardBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    public static Texture cardImage = null;

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard"
    )
    public static class FlipCardEffectPatches1 {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if (cardScaleX < 1.0f) {
                target_x = _instance.target_x;
                target_y = _instance.target_y;
                current_x = _instance.current_x;
                current_y = _instance.current_y;

                targetAngle = _instance.targetAngle;
                angle = _instance.angle;

                _instance.target_x = Settings.WIDTH / 2.0f;
                _instance.target_y = Settings.HEIGHT / 2.0f;
                _instance.current_x = Settings.WIDTH / 2.0f;
                _instance.current_y = Settings.HEIGHT / 2.0f;

                _instance.setAngle(0.0f, true);


                sb.end();
                cardBuffer.begin();

                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);

                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
                sb.begin();
                sb.setColor(Color.WHITE);
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard"
    )
    public static class FlipCardEffectPatches2 {
        @SpirePostfixPatch
        public static SpireReturn<Void> Postfix(AbstractCard _instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if (cardScaleX < 1.0f) {
                sb.end();
                cardBuffer.end();

                cardImage = cardBuffer.getColorBufferTexture();
                sb.begin();

                _instance.target_x = target_x;
                _instance.target_y = target_y;
                _instance.current_x = current_x;
                _instance.current_y = current_y;

                _instance.targetAngle = targetAngle;
                _instance.angle = angle;

                sb.draw(cardImage, target_x - cardImage.getWidth() / 2.0f, target_y - cardImage.getHeight() / 2.0f,
                        cardImage.getWidth() / 2.0f, cardImage.getHeight() / 2.0f, cardImage.getWidth(), cardImage.getHeight(),
                        cardScaleX, 1.0f,
                        angle, 0, 0, cardImage.getWidth(), cardImage.getHeight(), false, true);
            }

            return SpireReturn.Continue();
        }
    }


    public static float flipBgOffset = -1.0f;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class FlipCardEffectScreenPatches1 {
        @SpireInsertPatch(rloc = 2670 - 2658)
        public static SpireReturn<Void> Insert(AbstractDungeon _instance, SpriteBatch sb) {
            if (AbstractDungeon.rs == AbstractDungeon.RenderScene.NORMAL) {


                Texture oriImage = ShionMaskHelper.getScreenFlipTexture(sb, sb.getColor(), sb1 -> {
                    AbstractDungeon.scene.renderCombatRoomBg(sb);
                });

                Texture maskImage = ShionMaskHelper.getScreenMaskTexture(sb, sb2 -> {
                    sb2.draw(ImageMaster.WHITE_SQUARE_IMG,
                            Settings.WIDTH * flipBgOffset, 0.0f,
                            0.0F, 0.0f,
                            32.0f, 32.0f,
                            Settings.WIDTH / 32.0f, Settings.HEIGHT / 32.0f,
                            0.0f,
                            0, 0,
                            32, 32,
                            false, false);
                }, false);

                sb.end();
                sb.setShader(ShionMaskHelper.alphaMaskShader);
                maskImage.bind(1);
                oriImage.bind(0);
                sb.begin();
                sb.setColor(Color.WHITE);
                ShionMaskHelper.alphaMaskShader.setUniformi("u_mask", 1);

                sb.draw(oriImage,
                        0,
                        0,
                        0.0F, 0.0f,
                        Settings.WIDTH, Settings.HEIGHT,
                        1.0f, 1.0f,
                        0.0f,
                        0, 0,
                        Settings.WIDTH, Settings.HEIGHT,
                        false, true);

                sb.flush();
                sb.end();
                sb.setShader(null);
                sb.begin();

            }

            return SpireReturn.Continue();
        }
    }
}
