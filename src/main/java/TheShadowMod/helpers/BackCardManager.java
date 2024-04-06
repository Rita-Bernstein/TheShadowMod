package TheShadowMod.helpers;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.cards.TheShadow.Defend_TS;
import TheShadowMod.cards.TheShadow.Strike_TS;
import TheShadowMod.patches.CardTagsEnum;
import TheShadowMod.patches.FlipCardEffectPatches;
import TheShadowMod.powers.TheShadow.AnnihilatePower;
import TheShadowMod.powers.TheShadow.RealityFormPower;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_HEIGHT;
import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_WIDTH;

public class BackCardManager {

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class AddFields {
        public static SpireField<Boolean> isBack = new SpireField<>(() -> false);
        public static SpireField<Boolean> flipOnUseOnce = new SpireField<>(() -> false);
        public static SpireField<Boolean> doubleOnUseOnce = new SpireField<>(() -> false);

        public static SpireField<AbstractCard> backCard = new SpireField<>(() -> null);
    }

    public static boolean selectScreenTempFlipViewing = false;
    public static boolean oriIsViewingFlip = false;

    public static void flipSameSideBackgroundView(AbstractCard card) {
        if (AddFields.backCard.get(card) instanceof AbstractTSCard && AddFields.backCard.get(card) == card) {
            AddFields.isBack.set(card, !AddFields.isBack.get(card));
            setBackCardBackground((AbstractTSCard) card, AddFields.isBack.get(card));
            return;
        }

        if(AddFields.backCard.get(card) == null){
            AddFields.isBack.set(card, !AddFields.isBack.get(card));
            setBackCardBackground((AbstractTSCard) card, AddFields.isBack.get(card));
        }
    }

    public static AbstractCard flipCard(AbstractCard card) {
        if (AddFields.backCard.get(card) != null) {
            AbstractCard back = AddFields.backCard.get(card);

            back.target_x = card.target_x;
            back.target_y = card.target_y;
            back.current_x = card.current_x;
            back.current_y = card.current_y;

            back.drawScale = card.drawScale;
            back.targetDrawScale = card.targetDrawScale;
            back.angle = card.angle;
            back.targetAngle = card.targetAngle;


            back.hb.move(back.current_x, back.current_y);
            back.hb.resize(
                    (float) ReflectionHacks.getPrivate(back, AbstractCard.class, "HB_W") * back.drawScale,
                    (float) ReflectionHacks.getPrivate(back, AbstractCard.class, "HB_H") * back.drawScale
            );

            return back;
        }

        return card;
    }


    public static void onFlip(AbstractTSCard card) {
        card.onFlip(card, true);

        if (AddFields.backCard.get(card) instanceof AbstractTSCard) {
            ((AbstractTSCard) AddFields.backCard.get(card)).onFlip(card, false);
        }
    }

    public static void onFlipInHand(AbstractTSCard card) {
        card.onFlipInHand(card, true);

        if (AddFields.backCard.get(card) instanceof AbstractTSCard) {
            ((AbstractTSCard) AddFields.backCard.get(card)).onFlipInHand(card, false);
        }
    }


    public static void useFlipCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            m = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }


        if (AddFields.backCard.get(card) != null) {
            AddFields.backCard.get(card).freeToPlayOnce = card.freeToPlayOnce;
            AddFields.backCard.get(card).energyOnUse = card.energyOnUse;
        }

        if (card instanceof AbstractTSCard) {
            ((AbstractTSCard) card).useCommon(p, m);
        }

        if (AddFields.backCard.get(card) instanceof AbstractTSCard) {
            ((AbstractTSCard) AddFields.backCard.get(card)).useCommon(p, m);
        }


        if (canDoubleTrigger(card) && AddFields.backCard.get(card) != null) {
            AddFields.doubleOnUseOnce.set(card, false);
            AddFields.doubleOnUseOnce.set(AddFields.backCard.get(card), false);


            if (card instanceof AbstractTSCard) {
                ((AbstractTSCard) card).useCommon(p, m);
            }

            if (AddFields.backCard.get(card) instanceof AbstractTSCard) {
                ((AbstractTSCard) AddFields.backCard.get(card)).useCommon(p, m);
            }

            AddFields.backCard.get(card).freeToPlayOnce = true;

            card.use(p, m);

            AddFields.backCard.get(card).use(p, m);

            return;
        }

        card.use(p, m);
    }


    public static boolean canDoubleTrigger(AbstractCard card) {
        if (AddFields.doubleOnUseOnce.get(card))
            return true;

        if (AddFields.backCard.get(card) != null && AddFields.doubleOnUseOnce.get(card))
            return true;

        if (AbstractDungeon.player.hasPower(AnnihilatePower.POWER_ID)) {
            return true;
        }

        if (AbstractDungeon.player.hasPower(RealityFormPower.POWER_ID) && !card.purgeOnUse) {
            RealityFormPower p = (RealityFormPower) AbstractDungeon.player.getPower(RealityFormPower.POWER_ID);
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() <= p.amount) {
                return true;
            }
        }


        return false;
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class}
    )
    public static class PlayFlipCardPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace(BackCardManager.class.getName() + ".useFlipCard($0,$1,$2);"
                        );
                    }
                }
            };
        }
    }


    public static void setBackCardBackground(AbstractTSCard c, boolean isViewingBack) {
        if (isViewingBack) {
            switch (c.type) {
                case ATTACK:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_attack_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_attack_lime2.png");
                    break;
                case POWER:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_power_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_power_lime2.png");
                    break;
                default:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_skill_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_skill_lime2.png");
                    break;
            }
        } else {
            switch (c.type) {
                case ATTACK:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_attack_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_attack_lime.png");
                    break;
                case POWER:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_power_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_power_lime.png");
                    break;
                default:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_skill_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_skill_lime.png");
                    break;
            }
        }
    }


    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class BackPowerCardPowerPatch {
        @SpireInsertPatch(rloc = 108 - 84)
        public static SpireReturn<Void> Insert(UseCardAction _instance) {
            AbstractCard targetCard = ReflectionHacks.getPrivate(_instance, UseCardAction.class, "targetCard");

            if (canDoubleTrigger(targetCard))
                if (AddFields.backCard.get(targetCard) != null && AddFields.backCard.get(targetCard).type == AbstractCard.CardType.POWER) {

                    AbstractDungeon.actionManager.addToTop(new ShowCardAction(targetCard));
                    if (Settings.FAST_MODE) {
                        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                    } else {
                        AbstractDungeon.actionManager.addToTop(new WaitAction(0.7F));
                    }
                    AbstractDungeon.player.hand.empower(targetCard);
                    _instance.isDone = true;
                    AbstractDungeon.player.hand.applyPowers();
                    AbstractDungeon.player.hand.glowCheck();
                    AbstractDungeon.player.cardInUse = null;

                    return SpireReturn.Return();
                }


            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, AbstractCreature.class}
    )
    public static class BackPowerCardExhaustPatch {
        @SpireInsertPatch(rloc = 25)
        public static SpireReturn<Void> Insert(UseCardAction _instance, AbstractCard card, AbstractCreature target) {
            if (canDoubleTrigger(card)) {
                if (AddFields.backCard.get(card) != null) {
                    if (AddFields.backCard.get(card).exhaust || AddFields.backCard.get(card).exhaustOnUseOnce) {
                        _instance.exhaustCard = true;
                    }
                }
            }


            return SpireReturn.Continue();
        }
    }


    public static boolean noLoopLock = false;

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class ApplyPowersPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).applyPowers();
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "update"
    )
    public static class UpdatePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).update();
                    noLoopLock = false;
                }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage",
            paramtypez = {AbstractMonster.class}
    )
    public static class CalculateCardDamagePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance, AbstractMonster mo) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).calculateCardDamage(mo);
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "canUse",
            paramtypez = {AbstractPlayer.class, AbstractMonster.class}
    )
    public static class CanUsePatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard _instance, AbstractPlayer p, AbstractMonster mo) {
            if (ViewFlipButton.isViewingFlip) {
                _instance.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:ViewingFlip").TEXT[0];
                return SpireReturn.Return(false);
            }

            if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                if (canDoubleTrigger(_instance)) {
                    if (!noLoopLock) {
                        noLoopLock = true;

                        boolean ftp = AddFields.backCard.get(_instance).freeToPlayOnce;

                        AddFields.backCard.get(_instance).freeToPlayOnce = true;
                        if (!AddFields.backCard.get(_instance).canUse(p, mo)) {
                            noLoopLock = false;
                            AddFields.backCard.get(_instance).freeToPlayOnce = ftp;
                            return SpireReturn.Return(false);
                        }
                        AddFields.backCard.get(_instance).freeToPlayOnce = ftp;
                    }

                    noLoopLock = false;
                }
            }


            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "canPlay"
    )
    public static class CanPlayPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard _instance) {
            if (ViewFlipButton.isViewingFlip) {
                _instance.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:ViewingFlip").TEXT[0];
                return SpireReturn.Return(false);
            }

            if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                if (canDoubleTrigger(_instance)) {
                    if (!noLoopLock) {
                        noLoopLock = true;
                        if (!AddFields.backCard.get(_instance).canPlay(AddFields.backCard.get(_instance))) {
                            noLoopLock = false;
                            return SpireReturn.Return(false);
                        }
                    }

                    noLoopLock = false;
                }
            }


            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "canUpgrade"
    )
    public static class CanUpgradePatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard _instance) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;

                    if (AddFields.backCard.get(_instance).canUpgrade()) {
                        noLoopLock = false;
                        return SpireReturn.Return(true);
                    }
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "updateCost"
    )
    public static class UpdateCostPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance, int amt) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).updateCost(amt);
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "setCostForTurn"
    )
    public static class SetCostForTurnPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance, int amt) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).setCostForTurn(amt);
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "modifyCostForCombat"
    )
    public static class ModifyCostForCombatPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance, int amt) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).modifyCostForCombat(amt);
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "resetAttributes"
    )
    public static class ResetAttributesPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCard _instance) {
            if (!noLoopLock)
                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    noLoopLock = true;
                    AddFields.backCard.get(_instance).resetAttributes();
                    noLoopLock = false;
                }

            return SpireReturn.Continue();
        }
    }


    public static void initializeBackCard(AbstractCard card) {
        if (card instanceof AbstractTSCard) {
            if (AddFields.backCard.get(card) == null && AbstractDungeon.player != null && AbstractDungeon.cardRandomRng != null) {
                if (card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.SPECIAL) {

                    AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
                    AbstractCard c;
                    switch (rarity) {
                        case COMMON:
                            AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
                            if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset) {
                                AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
                            }
                        case UNCOMMON:
                            break;
                        case RARE:
                            AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzStartOffset;
                            break;
                    }


                    AbstractCard tmp;


                    includeEssence = true;
                    do {
                        if (AbstractDungeon.player.hasRelic("PrismaticShard")) {
                            tmp = CardLibrary.getAnyColorCard(rarity);
                        } else {
                            tmp = AbstractDungeon.getCard(rarity);
                        }
                    } while (!correctBackCard(tmp));
                    includeEssence = false;

                    noNewBackCardLoop = true;
                    c = tmp.makeCopy();
                    noNewBackCardLoop = false;

                    if (c.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance")) && c.canUpgrade()) {
                        c.upgrade();
                    }

                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        r.onPreviewObtainCard(c);
                    }

                    setCardToBackCard(c, card, true);
                    return;

                } else {
                    if (card instanceof Strike_TS) {
                        noNewBackCardLoop = true;
                        AbstractCard newCard = new Defend_TS();
                        noNewBackCardLoop = false;
                        setCardToBackCard(newCard, card, true);
                        return;
                    }

                    if (card instanceof Defend_TS) {
                        noNewBackCardLoop = true;
                        AbstractCard newCard = new Strike_TS();
                        noNewBackCardLoop = false;
                        setCardToBackCard(newCard, card, true);
                        return;
                    }

                }
            }
        }

        AddFields.backCard.set(card, card);
    }


    public static boolean noNewBackCardLoop = false;
    public static boolean makeCopyOnlyCard = false;
    public static boolean includeEssence = false;

    public static void setBackCardFromIndex(AbstractCard card, String id, int upgrades) {
        noNewBackCardLoop = true;
        AbstractCard c = CardLibrary.getCard(id).makeCopy();
        noNewBackCardLoop = false;

        if (upgrades > 0) {
            for (int i = 0; i < upgrades; i++)
                c.upgrade();
        }


        setCardToBackCard(c, card, true);
    }

    public static boolean correctBackCard(AbstractCard card) {
        if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return !card.hasTag(AbstractCard.CardTags.HEALING);
        }

        return true;
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class,
                    AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class}
    )
    public static class MakeCopyPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _instance, String id, String name, String imgUrl, int cost, String rawDescription,
                                   AbstractCard.CardType type, AbstractCard.CardColor color,
                                   AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, DamageInfo.DamageType dType) {
            if (!noNewBackCardLoop && !makeCopyOnlyCard) {
                initializeBackCard(_instance);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeSameInstanceOf"
    )
    public static class MakeSameInstanceOfPatch {
        @SpireInsertPatch(rloc = 1, localvars = {"card"})
        public static SpireReturn<AbstractCard> Insert(AbstractCard _instance, @ByRef AbstractCard[] card) {
            if (AddFields.backCard.get(card[0]) != null) {
                AddFields.backCard.get(card[0]).uuid = _instance.uuid;
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopyPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractCard _instance) {
            makeCopyOnlyCard = true;
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopyPatch2 {
        @SpireInsertPatch(rloc = 1002 - 978, localvars = {"card"})
        public static SpireReturn<AbstractCard> Insert(AbstractCard _instance, @ByRef AbstractCard[] card) {
            AbstractCard bCardBuffer = null;

            AddFields.isBack.set(card[0], AddFields.isBack.get(_instance));

            if (AddFields.backCard.get(_instance) != null) {
                if (AddFields.backCard.get(_instance) == _instance) {
                    bCardBuffer = card[0];
                } else {
                    AddFields.backCard.set(AddFields.backCard.get(_instance), null);
                    bCardBuffer = AddFields.backCard.get(_instance).makeStatEquivalentCopy();
                    AddFields.backCard.set(AddFields.backCard.get(_instance), _instance);
                }
            } else if (_instance instanceof AbstractTSCard) {
                initializeBackCard(_instance);

            }

            makeCopyOnlyCard = false;

            if (bCardBuffer != null) {
                AddFields.backCard.set(card[0], bCardBuffer);
                AddFields.backCard.set(bCardBuffer, card[0]);
            }


            return SpireReturn.Continue();
        }
    }

    public static boolean noBackCardPreviewLoop = false;


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCardTip"
    )
    public static class RenderBackCardPatch {
        @SpireInsertPatch(rloc = 2975 - 2946)
        public static SpireReturn<Void> Insert(AbstractCard _instance, SpriteBatch sb) {
            if (!noBackCardPreviewLoop) {
                noBackCardPreviewLoop = true;

                if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                    sb.setColor(Color.WHITE);

                    AbstractCard backCard = AddFields.backCard.get(_instance);

                    if (_instance.current_x > Settings.WIDTH * 0.75F) {
                        backCard.current_x = _instance.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * _instance.drawScale;
                    } else {
                        backCard.current_x = _instance.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * _instance.drawScale;
                    }

                    if (_instance.cardsToPreview == null) {
                        backCard.current_y = _instance.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * _instance.drawScale;
                    } else {
                        if (_instance.current_y < Settings.HEIGHT * 0.5F) {
                            backCard.current_y = _instance.current_y + (IMG_HEIGHT / 2.0F + IMG_HEIGHT / 2.0F * 0.8F) * _instance.drawScale;
                        } else {
                            backCard.current_y = _instance.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT * 1.5F * 0.8F) * _instance.drawScale;
                        }
                    }

                    backCard.drawScale = _instance.drawScale * 0.8F;
                    backCard.setAngle(0.0f, true);

                    backCard.hb.move(backCard.current_x, backCard.current_y);
                    backCard.hb.resize(
                            (float) ReflectionHacks.getPrivate(backCard, AbstractCard.class, "HB_W") * backCard.drawScale,
                            (float) ReflectionHacks.getPrivate(backCard, AbstractCard.class, "HB_H") * backCard.drawScale
                    );

                    backCard.render(sb);
                }

                noBackCardPreviewLoop = false;
            }


            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderTips"
    )
    public static class RenderCardPreviewBackInSingleView {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _instance, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");

            if (AddFields.backCard.get(card) != null && !AddFields.backCard.get(card).cardID.equals(card.cardID)) {


                renderCardPreviewBackInSingleView(AddFields.backCard.get(card), sb);
            }
        }

    }

    public static void renderCardPreviewBackInSingleView(AbstractCard card, SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        card.current_x = Settings.WIDTH - 1435.0F * Settings.scale;
        card.current_y = Settings.HEIGHT - 795.0F * Settings.scale;
        card.drawScale = 0.8F;
        card.render(sb);
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = SpirePatch.CLASS
    )
    public static class SpireFieldPortraitImgBack {
        public static SpireField<Texture> portraitImgBack = new SpireField<>(() -> null);
    }


    public static Texture portraitToSave = null;


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardPatch {
        @SpireInsertPatch(rloc = 1, localvars = {"copy"})
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb, @ByRef AbstractCard[] copy) {
            if (ViewFlipButton.isViewingFlip) {
                AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                copy[0] = c.makeStatEquivalentCopy();

                if (AddFields.backCard.get(c) != null && AddFields.backCard.get(c) != c) {
                    ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "card", AddFields.backCard.get(c));

                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardFlipViewPatch {
        @SpireInsertPatch(rloc = 6, localvars = {"copy"})
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb, @ByRef AbstractCard[] copy) {
            if (ViewFlipButton.isViewingFlip) {
                AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                if(c instanceof AbstractTSCard && (AddFields.backCard.get(c) == null || AddFields.backCard.get(c) == c)) {
                    setBackCardBackground((AbstractTSCard) c, !AddFields.isBack.get(c));
                }

            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "loadPortraitImg"
    )
    public static class RenderSingleBackCardImagePatch {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _instance) {
            AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
            AbstractCard card;
            if (BackCardManager.AddFields.backCard.get(c) != null && BackCardManager.AddFields.backCard.get(c) != c) {
                card = BackCardManager.AddFields.backCard.get(c);
            }else {
                card = c;
            }

            if (card instanceof CustomCard) {
                SpireFieldPortraitImgBack.portraitImgBack.set(_instance, CustomCard.getPortraitImage((CustomCard) card));
            } else {
                if (Settings.PLAYTESTER_ART_MODE || UnlockTracker.betaCardPref.getBoolean(card.cardID, false)) {
                    SpireFieldPortraitImgBack.portraitImgBack.set(_instance, ImageMaster.loadImage("images/1024PortraitsBeta/" + card.assetUrl + ".png"));
                } else {
                    SpireFieldPortraitImgBack.portraitImgBack.set(_instance, ImageMaster.loadImage("images/1024Portraits/" + card.assetUrl + ".png"));
                    if (SpireFieldPortraitImgBack.portraitImgBack.get(_instance) == null) {
                        SpireFieldPortraitImgBack.portraitImgBack.set(_instance, ImageMaster.loadImage("images/1024PortraitsBeta/" + card.assetUrl + ".png"));
                    }
                }

            }
        }
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardImagePatch2 {
        @SpireInsertPatch(rloc = 11)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (ViewFlipButton.isViewingFlip) {
                portraitToSave = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "portraitImg");
                AbstractCard c = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                if (c instanceof CustomCard) {
                    ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "portraitImg",
                            SpireFieldPortraitImgBack.portraitImgBack.get(_instance));
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render"
    )
    public static class RenderSingleBackCardImagePatch3 {
        @SpireInsertPatch(rloc = 12)
        public static SpireReturn<Void> Insert(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (ViewFlipButton.isViewingFlip) {
                ReflectionHacks.setPrivate(_instance, SingleCardViewPopup.class, "portraitImg", portraitToSave);
            }

            return SpireReturn.Continue();
        }
    }


    //    输入卡牌本体进来！！！！！！！！ 正反面外面判断吧
    public static AbstractCard setCardToFrontCard(AbstractCard fromCard, AbstractCard toCard, boolean currentSide) {
        AbstractCard finalCard;
        if (fromCard instanceof AbstractTSCard) {
            if (!currentSide)
                finalCard = AddFields.backCard.get(fromCard);
            else
                finalCard = fromCard;
        } else {
            finalCard = fromCard;
        }

        AddFields.backCard.set(finalCard, AddFields.backCard.get(toCard));
        AddFields.backCard.set(AddFields.backCard.get(toCard), finalCard);

        AddFields.isBack.set(toCard, true);
        AddFields.isBack.set(finalCard, false);

        if (finalCard instanceof AbstractTSCard)
            setBackCardBackground((AbstractTSCard) finalCard, false);

        if (AddFields.backCard.get(finalCard) instanceof AbstractTSCard && AddFields.backCard.get(finalCard) != finalCard) {
            setBackCardBackground((AbstractTSCard) AddFields.backCard.get(finalCard), true);
        }


        return finalCard;
    }


    //    输入卡牌本体进来！！！！！！！！ 正反面外面判断吧
    public static AbstractCard setCardToBackCard(AbstractCard fromCard, AbstractCard toCard, boolean currentSide) {
        AbstractCard finalCard;
        if (fromCard instanceof AbstractTSCard) {
            if (!currentSide)
                finalCard = AddFields.backCard.get(fromCard);
            else
                finalCard = fromCard;
        } else {
            finalCard = fromCard;
        }

        AddFields.backCard.set(toCard, finalCard);
        AddFields.backCard.set(finalCard, toCard);

        AddFields.isBack.set(toCard, false);
        AddFields.isBack.set(finalCard, true);

        if (toCard instanceof AbstractTSCard)
            setBackCardBackground((AbstractTSCard) toCard, false);

        if (AddFields.backCard.get(toCard) instanceof AbstractTSCard && AddFields.backCard.get(toCard) != toCard) {
            setBackCardBackground((AbstractTSCard) AddFields.backCard.get(toCard), true);
        }

        return toCard;
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "clickAndDragCards"
    )
    public static class CardTargetPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("ENEMY") && f.getClassName().equals(AbstractCard.CardTarget.class.getName())) {
                        f.replace("$_ =" + BackCardManager.class.getName() + ".getCardTarget(this.hoveredCard); "
                        );
                    }
                }
            };
        }
    }

    //    与卡牌的指向类型相同表示 为 ENEMY
    public static AbstractCard.CardTarget getCardTarget(AbstractCard hoverCard) {
        if (hoverCard instanceof AbstractTSCard) {
            AbstractTSCard card = (AbstractTSCard) hoverCard;

            if (canDoubleTrigger(card)) {
                if (AddFields.backCard.get(card) != null) {
                    if (AddFields.backCard.get(card).target == AbstractCard.CardTarget.ENEMY)
                        return hoverCard.target;
                }
            }
        }

        return AbstractCard.CardTarget.ENEMY;
    }


    @SpirePatch(
            clz = SaveAndContinue.class,
            method = "save",
            paramtypez = {SaveFile.class}
    )
    public static class OnSavePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SaveFile saveFile) {
            SaveHelper.saveDeckBackCard();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "loadPlayerSave"
    )
    public static class PatchLoadPlayer {
        @SpireInsertPatch(rloc = 68)
        public static SpireReturn<Void> Insert(CardCrawlGame _instance, AbstractPlayer p) {
            SaveHelper.loadDeckBackCard(p);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class FlipCardOnceUsePatch {
        @SpireInsertPatch(rloc = 126 - 84)
        public static SpireReturn<Void> Insert(UseCardAction _instance) {
            AbstractCard targetCard = ReflectionHacks.getPrivate(_instance, UseCardAction.class, "targetCard");

            if (AddFields.flipOnUseOnce.get(targetCard)) {
                AddFields.flipOnUseOnce.set(targetCard, false);

                if (AddFields.backCard.get(targetCard) != null && AddFields.backCard.get(targetCard) != targetCard)

                    ReflectionHacks.setPrivate(_instance, UseCardAction.class, "targetCard", AddFields.backCard.get(targetCard));
            }

            return SpireReturn.Continue();
        }
    }

//    @SpirePatch(
//            clz = HandCardSelectScreen.class,
//            method = "render"
//    )
//    public static class HandCardSelectTempFlipViewingPatch {
//        @SpireInsertPatch(rloc = 673 - 651)
//        public static SpireReturn<Void> Insert(HandCardSelectScreen _instance,SpriteBatch sb) {
//                if(selectScreenTempFlipViewing){
//                    oriIsViewingFlip = ViewFlipButton.isViewingFlip;
//                    ViewFlipButton.isViewingFlip = true;
//                }
//
//
//            return SpireReturn.Continue();
//        }
//    }
//    @SpirePatch(
//            clz = HandCardSelectScreen.class,
//            method = "render"
//    )
//    public static class HandCardSelectTempFlipViewingPatch2 {
//        @SpireInsertPatch(rloc = 674 - 651)
//        public static SpireReturn<Void> Insert(HandCardSelectScreen _instance,SpriteBatch sb) {
//                if(selectScreenTempFlipViewing){
//                    ViewFlipButton.isViewingFlip = oriIsViewingFlip;
//                }
//
//
//            return SpireReturn.Continue();
//        }
//    }


    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "render"
    )
    public static class HandCardSelectTempFlipViewingPatch {
        private static int count = 0;

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("render")) {
                        count++;
                        if (count == 2)
                            m.replace("if(" + BackCardManager.class.getName() + ".selectScreenTempFlipViewing){"
                                    + BackCardManager.class.getName() + ".renderHandCardSelectTempFlip($1,this.selectedCards);"
                                    + "}else{"
                                    + "$proceed($$);"
                                    + "}"
                            );

                    }
                }
            };
        }
    }

    public static void renderHandCardSelectTempFlip(SpriteBatch sb, CardGroup group) {
        for (AbstractCard _instance : group.group) {
            if (AddFields.backCard.get(_instance) != null && AddFields.backCard.get(_instance) != _instance) {
                sb.setColor(Color.WHITE);

                AbstractCard backCard = AddFields.backCard.get(_instance);
                backCard.current_x = _instance.current_x;
                backCard.current_y = _instance.current_y;

                backCard.drawScale = _instance.drawScale;
                backCard.setAngle(0.0f, true);

                backCard.hb.move(backCard.current_x, backCard.current_y);
                backCard.hb.resize(
                        (float) ReflectionHacks.getPrivate(backCard, AbstractCard.class, "HB_W") * backCard.drawScale,
                        (float) ReflectionHacks.getPrivate(backCard, AbstractCard.class, "HB_H") * backCard.drawScale
                );

                backCard.render(sb);
            } else {
                _instance.render(sb);
            }
        }
    }


// =============    正面不生成本质

    @SpirePatch(
            clz = CardGroup.class,
            method = "getRandomCard",
            paramtypez = {Random.class}
    )
    public static class NoEssenceCardPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup _instance, Random rng) {
            if (!includeEssence) {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (AbstractCard c : _instance.group) {
                    if (!c.hasTag(CardTagsEnum.Essence)) {
                        group.addToTop(c);
                    }
                }

                return SpireReturn.Return(group.group.get(rng.random(group.group.size() - 1)));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "getRandomCard",
            paramtypez = {boolean.class}
    )
    public static class NoEssenceCardPatch2 {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup _instance, boolean useRng) {
            if (!includeEssence) {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (AbstractCard c : _instance.group) {
                    if (!c.hasTag(CardTagsEnum.Essence)) {
                        group.addToTop(c);
                    }
                }

                if (useRng) {
                    return SpireReturn.Return(group.group.get(AbstractDungeon.cardRng.random(group.group.size() - 1)));
                } else {
                    return SpireReturn.Return(group.group.get(MathUtils.random(group.group.size() - 1)));
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "getRandomCard",
            paramtypez = {boolean.class, AbstractCard.CardRarity.class}
    )
    public static class NoEssenceCardPatch3 {
        @SpireInsertPatch(rloc = 6, localvars = {"tmp"})
        public static SpireReturn<AbstractCard> Insert(CardGroup _instance, boolean useRng, AbstractCard.CardRarity rarity, @ByRef ArrayList<AbstractCard>[] tmp) {
            if (!includeEssence)
                tmp[0].removeIf(card -> card.hasTag(CardTagsEnum.Essence));

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "getRandomCard",
            paramtypez = {Random.class, AbstractCard.CardRarity.class}
    )
    public static class NoEssenceCardPatch4 {
        @SpireInsertPatch(rloc = 6, localvars = {"tmp"})
        public static SpireReturn<AbstractCard> Insert(CardGroup _instance, Random rng, AbstractCard.CardRarity rarity, @ByRef ArrayList<AbstractCard>[] tmp) {
            if (!includeEssence)
                tmp[0].removeIf(card -> card.hasTag(CardTagsEnum.Essence));

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "getRandomCard",
            paramtypez = {AbstractCard.CardType.class, boolean.class}
    )
    public static class NoEssenceCardPatch5 {
        @SpireInsertPatch(rloc = 6, localvars = {"tmp"})
        public static SpireReturn<AbstractCard> Insert(CardGroup _instance, AbstractCard.CardType type, boolean useRng, @ByRef ArrayList<AbstractCard>[] tmp) {
            if (!includeEssence)
                tmp[0].removeIf(card -> card.hasTag(CardTagsEnum.Essence));

            return SpireReturn.Continue();
        }
    }

    // =============    翻转中卡牌不能被选取


    @SpirePatch(
            clz = CardGroup.class,
            method = "getHoveredCard"
    )
    public static class NotHoverLogic {
        @SpireInsertPatch(rloc = 10, localvars = {"c"})
        public static SpireReturn<AbstractCard> Insert(CardGroup _instance, AbstractCard c) {
            if (FlipCardEffectPatches.cardScaleX < 1.0f || ViewFlipButton.isViewingFlip) {
                ViewFlipButton button = ViewFlipButton.getButton();
                TipHelper.renderGenericTip(button.hb.cX, button.hb.cY - 120.0f * Settings.scale, ViewFlipButton.uiStrings.TEXT[0], ViewFlipButton.uiStrings.TEXT[2]);
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

//    @SpirePatch(
//            cls ="basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.BackgroundFix$BackgroundTexture",
//            method = "Prefix"
//    )
//    public static class BaseCardSingleRenderPatch {
//        @SpireInsertPatch(rlocs = {42-30,57-30,87-30})
//        public static SpireReturn<AbstractCard> Insert(Object __obj_instance, Object sbObject) {
//            if (ViewFlipButton.isViewingFlip) {
//
//                return SpireReturn.Return(null);
//            }
//
//            return SpireReturn.Continue();
//        }
//    }
}
