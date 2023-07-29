package TheShadowMod.helpers;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
                flipViewAllCards(isViewingFlip);
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

    public static void flipViewAllCards(boolean isViewingFlip) {
        if (AbstractDungeon.player.cardInUse instanceof AbstractTSCard) {
            ((AbstractTSCard) AbstractDungeon.player.cardInUse).isViewingFlip = isViewingFlip;
            ((AbstractTSCard) AbstractDungeon.player.cardInUse).onViewingFlip();
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.limbo.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

//        卡组是实际翻转
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = isViewingFlip;
                ((AbstractTSCard) c).onViewingFlip();
            }
        }

//        卡牌奖励

        if(AbstractDungeon.cardRewardScreen.rewardGroup != null && AbstractDungeon.cardRewardScreen.rewardGroup.size() >0){
            for (AbstractCard c : AbstractDungeon.cardRewardScreen.rewardGroup) {
                if (c instanceof AbstractTSCard) {
                    ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                    ((AbstractTSCard) c).onViewingFlip();
                }
            }
        }
        if(AbstractDungeon.shopScreen.coloredCards != null && AbstractDungeon.shopScreen.coloredCards.size() >0){
            for (AbstractCard c : AbstractDungeon.shopScreen.coloredCards) {
                if (c instanceof AbstractTSCard) {
                    ((AbstractTSCard) c).isViewingFlip = isViewingFlip;
                    ((AbstractTSCard) c).onViewingFlip();
                }
            }
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


    public static AbstractCard copyToSave = null;
    public static Texture portraitToSave = null;


    public static void changePortrait(AbstractCard card) {

    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardPatch {
        @SpireInsertPatch(rloc = 1)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (ViewFlipButton.isViewingFlip) {
                AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                copyToSave = c.makeStatEquivalentCopy();

                if (c instanceof AbstractTSCard && ((AbstractTSCard) c).backCard != null) {
                    ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "card", ((AbstractTSCard) c).backCard);

                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardPatch2 {
        @SpireInsertPatch(rloc = 6, localvars = {"copy"})
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb, @ByRef AbstractCard[] copy) {
            if (ViewFlipButton.isViewingFlip)
                copy[0] = copyToSave;

            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardPatch3 {
        @SpireInsertPatch(rloc = 11)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (ViewFlipButton.isViewingFlip) {
                portraitToSave = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "portraitImg");
                AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                if (c instanceof CustomCard) {
                    ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "portraitImg",
                            CustomCard.getPortraitImage((CustomCard) c));
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardPatch4 {
        @SpireInsertPatch(rloc = 12)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (ViewFlipButton.isViewingFlip) {
                ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "portraitImg", portraitToSave);
            }

            return SpireReturn.Continue();
        }
    }
}
