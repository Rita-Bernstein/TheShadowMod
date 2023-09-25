package TheShadowMod.helpers;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ViewFlipButton {
    private static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.4F);
    private static final float SHOW_X = 140.0F * Settings.scale;
    private static final float DRAW_Y = Settings.HEIGHT * 0.75f;
    private static final float HIDE_X = SHOW_X - 400.0F * Settings.scale;
    private float current_x;
    private float target_x;
    private boolean isHidden;
    public static boolean isViewingFlip = false;
    private float particleTimer;
    public Hitbox hb;

    public ViewFlipButton() {
        this.current_x = HIDE_X;
        this.target_x = this.current_x;
        this.isHidden = true;
        this.particleTimer = 0.0F;
        this.hb = new Hitbox(170.0F * Settings.scale, 170.0F * Settings.scale);
        this.hb.move(SHOW_X, DRAW_Y);
    }

    public void update() {
        if (!this.isHidden) {
            this.hb.update();
            if (InputHelper.justClickedLeft && this.hb.hovered) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (this.hb.justHovered) {
                CardCrawlGame.sound.play("UI_HOVER");
            }
        }

        if (this.hb.clicked) {
            this.hb.clicked = false;
            isViewingFlip = !isViewingFlip;
            if (AbstractDungeon.player != null)
                flipViewAllCards();
        }

        if (isViewingFlip) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.2F;
                AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.hb.cX, this.hb.cY, Color.SKY));
                AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.hb.cX, this.hb.cY, Color.WHITE));
            }
        }

        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
            }
        }


    }


    public void hide() {
        if (!this.isHidden) {
            this.target_x = HIDE_X;
            this.isHidden = true;
        }

    }

    public void show() {
        if (this.isHidden) {
            isViewingFlip = false;
            this.target_x = SHOW_X;
            this.isHidden = false;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderButton(sb);

        if (isViewingFlip) {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(0.6F, 1.0F, 1.0F, 1.0F));

            float derp = Interpolation.swingOut.apply(1.0F, 1.1F, MathUtils.cosDeg((float) (System.currentTimeMillis() / 4L % 360L)) / 12.0F);

            sb.draw(ImageMaster.PEEK_BUTTON, this.current_x - 64.0F, DRAW_Y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F,
                    Settings.scale * derp, Settings.scale * derp, 0.0F, 0, 0, 128, 128, false, false);

            sb.setBlendFunction(770, 771);
        }

        if (this.hb.hovered && !this.hb.clickStarted) {
            sb.setBlendFunction(770, 1);
            sb.setColor(HOVER_BLEND_COLOR);
            renderButton(sb);
            sb.setBlendFunction(770, 771);
        }


        if (!this.isHidden) {
            this.hb.render(sb);
        }

    }

    private void renderButton(SpriteBatch sb) {
        sb.draw(ImageMaster.PEEK_BUTTON, this.current_x - 64.0F, DRAW_Y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
    }


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = SpirePatch.CLASS
    )
    public static class PatchField {
        public static SpireField<ViewFlipButton> viewFlipButton = new SpireField<>(() -> new ViewFlipButton());

    }

    public static ViewFlipButton getButton() {
        return PatchField.viewFlipButton.get(CardCrawlGame.dungeon);
    }


    public static boolean ifCombatHasFlippable() {
        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
                if (AbstractDungeon.player.cardInUse instanceof AbstractTSCard) {
                    return true;
                }

                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof AbstractTSCard) {
                        return true;
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                    if (c instanceof AbstractTSCard) {
                        return true;
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c instanceof AbstractTSCard) {
                        return true;
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                    if (c instanceof AbstractTSCard) {
                        return true;
                    }
                }

                for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                    if (c instanceof AbstractTSCard) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void flipViewAllCards() {

        if (AbstractDungeon.player.cardInUse != null) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.cardInUse);
            AbstractDungeon.player.cardInUse = BackCardManager.flipCard(AbstractDungeon.player.cardInUse);
        }


        for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.hand.group.get(i));
            AbstractDungeon.player.hand.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.hand.group.get(i)));
        }

        for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.drawPile.group.get(i));
            AbstractDungeon.player.drawPile.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.drawPile.group.get(i)));
        }

        for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.discardPile.group.get(i));
            AbstractDungeon.player.discardPile.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.discardPile.group.get(i)));
        }

        for (int i = 0; i < AbstractDungeon.player.exhaustPile.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.exhaustPile.group.get(i));
            AbstractDungeon.player.exhaustPile.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.exhaustPile.group.get(i)));
        }

        for (int i = 0; i < AbstractDungeon.player.limbo.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.limbo.group.get(i));
            AbstractDungeon.player.limbo.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.limbo.group.get(i)));
        }

        for (int i = 0; i < AbstractDungeon.player.masterDeck.group.size(); i++) {
            BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.masterDeck.group.get(i));
            AbstractDungeon.player.masterDeck.group.set(i, BackCardManager.flipCard(AbstractDungeon.player.masterDeck.group.get(i)));
        }


//        卡牌奖励

        if (AbstractDungeon.cardRewardScreen.rewardGroup != null && AbstractDungeon.cardRewardScreen.rewardGroup.size() > 0) {
            for (int i = 0; i < AbstractDungeon.cardRewardScreen.rewardGroup.size(); i++) {
                BackCardManager.flipSameSideBackgroundView(AbstractDungeon.cardRewardScreen.rewardGroup.get(i));
                AbstractDungeon.cardRewardScreen.rewardGroup.set(i, BackCardManager.flipCard(AbstractDungeon.cardRewardScreen.rewardGroup.get(i)));
            }
        }

        if (AbstractDungeon.gridSelectScreen.targetGroup != null && AbstractDungeon.gridSelectScreen.targetGroup.size() > 0) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.targetGroup.size(); i++) {
                BackCardManager.flipSameSideBackgroundView(AbstractDungeon.gridSelectScreen.targetGroup.group.get(i));
                AbstractDungeon.gridSelectScreen.targetGroup.group.set(i, BackCardManager.flipCard(AbstractDungeon.gridSelectScreen.targetGroup.group.get(i)));
            }
        }


        if (AbstractDungeon.shopScreen.coloredCards != null && AbstractDungeon.shopScreen.coloredCards.size() > 0) {
            for (int i = 0; i < AbstractDungeon.shopScreen.coloredCards.size(); i++) {
                BackCardManager.flipSameSideBackgroundView(AbstractDungeon.shopScreen.coloredCards.get(i));
                AbstractDungeon.shopScreen.coloredCards.set(i, BackCardManager.flipCard(AbstractDungeon.shopScreen.coloredCards.get(i)));
            }
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PreBattlePrepPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            if (ViewFlipButton.isViewingFlip) {
                flipViewAllCards();
                isViewingFlip = false;
            }


            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class OnVictoryPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            if (ViewFlipButton.isViewingFlip) {
                flipViewAllCards();
                isViewingFlip = false;
            }
            return SpireReturn.Continue();
        }
    }



    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "update"
    )
    public static class PatchUpdate {
        @SpireInsertPatch(rloc = 30)
        public static void Insert(CardCrawlGame _instance) {
            if (CardCrawlGame.dungeon != null) {
                if (AbstractDungeon.player != null) {
                    getButton().update();

                    if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c instanceof AbstractTSCard)
                            && AbstractDungeon.currMapNode != null && !(AbstractDungeon.getCurrRoom() instanceof EventRoom)
                            || ifCombatHasFlippable()) {
                        getButton().show();

                    } else {
                        getButton().hide();
                    }
                } else {
                    getButton().hide();
                }
            }
        }
    }


    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "render"
    )
    public static class PatchRender {
        @SpireInsertPatch(rloc = 52)
        public static void Insert(CardCrawlGame _instance) {
            if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null)
                if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c instanceof AbstractTSCard)
                        && AbstractDungeon.currMapNode != null && !(AbstractDungeon.getCurrRoom() instanceof EventRoom)
                        || ifCombatHasFlippable())
                    getButton().render(ReflectionHacks.getPrivate(_instance, CardCrawlGame.class, "sb"));
        }
    }


//    大图按钮防退出

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "updateInput"
    )
    public static class ClosePatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("close") && m.getLineNumber() == 291) {
                        m.replace("if(" + ViewFlipButton.class.getName() + ".checkClickToClose()){ "
                                + "$proceed($$);"
                                + "}"

                        );
                    }
                }
            };
        }
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "updateInput"
    )
    public static class DontChangeClickedCaseyByRita {
        @SpireInsertPatch(rloc = 17)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance) {
            if (!checkClickToClose()) {
                InputHelper.justClickedLeft = true;
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "updateInput"
    )
    public static class ClearSCPFontTextures {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("ClearSCPFontTextures") && m.getLineNumber() == 293) {
                        m.replace("if(" + ViewFlipButton.class.getName() + ".checkClickToClose()){ "
                                + "$proceed($$);"
                                + "}"
                        );
                    }
                }
            };
        }
    }

    public static boolean checkClickToClose() {
        if (CardCrawlGame.dungeon != null)
            if (ViewFlipButton.getButton() != null && !ViewFlipButton.getButton().isHidden) {
                return !ViewFlipButton.getButton().hb.hovered;
            }
        return true;
    }
}
