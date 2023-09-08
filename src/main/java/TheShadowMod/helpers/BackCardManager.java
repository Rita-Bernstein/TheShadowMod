package TheShadowMod.helpers;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.cards.TheShadow.PlaceHolderCard;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class BackCardManager {

    //    输入卡牌本体进来！！！！！！！！ 正反面外面判断吧
    public static AbstractTSCard setCardToFrontCard(AbstractCard fromCard, AbstractCard toCard, boolean currentSide) {
        if (fromCard instanceof AbstractTSCard) {
            if (!currentSide) {
                if (((AbstractTSCard) fromCard).backCard instanceof AbstractTSCard) {
                    AbstractTSCard t = (AbstractTSCard) ((AbstractTSCard) fromCard).backCard;
                    t.thisCopy = t;

                    if (((AbstractTSCard) fromCard).thisCopy == null || ((AbstractTSCard) fromCard).thisCopy == fromCard) {
                        t.backCard = fromCard;
                    } else {
                        t.backCard = ((AbstractTSCard) fromCard).thisCopy;
                    }
                }

            }
            if (toCard instanceof AbstractTSCard) {

                ((AbstractTSCard) fromCard).backCard = ((AbstractTSCard) toCard).backCard;
            } else {
                ((AbstractTSCard) fromCard).backCard = toCard;
            }

            return (AbstractTSCard) fromCard;
        } else {
            AbstractTSCard card = new PlaceHolderCard();

            cloneFieldToCard(card, fromCard);

            card.thisCopy = fromCard;
            if (toCard instanceof AbstractTSCard) {
                card.backCard = ((AbstractTSCard) toCard).backCard;
            } else {
                card.backCard = toCard;
            }
            return card;
        }
    }


    //    输入卡牌本体进来！！！！！！！！ 正反面外面判断吧
    public static AbstractTSCard setCardToBackCard(AbstractCard fromCard, AbstractCard toCard, boolean currentSide) {
        if (toCard instanceof AbstractTSCard) {
            if (fromCard instanceof AbstractTSCard) {
                if (currentSide == ((AbstractTSCard) fromCard).isFlip) {
                    ((AbstractTSCard) toCard).backCard = ((AbstractTSCard) fromCard).backCard;
                } else {
                    if (((AbstractTSCard) fromCard).thisCopy == null || ((AbstractTSCard) fromCard).thisCopy == fromCard) {
                        ((AbstractTSCard) toCard).backCard = fromCard;
                    } else {
                        ((AbstractTSCard) toCard).backCard = ((AbstractTSCard) fromCard).thisCopy;
                    }
                }
            } else {
                ((AbstractTSCard) toCard).backCard = toCard;
            }

            return (AbstractTSCard) toCard;
        } else {

            AbstractTSCard card = new PlaceHolderCard();


            cloneFieldToCard(card, toCard);
            card.thisCopy = toCard;

            if (fromCard instanceof AbstractTSCard) {
                if (currentSide == ((AbstractTSCard) fromCard).isFlip) {
                    card.backCard = ((AbstractTSCard) fromCard).backCard;
                } else {
                    if (((AbstractTSCard) fromCard).thisCopy == null || ((AbstractTSCard) fromCard).thisCopy == fromCard)
                        card.backCard = fromCard;
                    else
                        card.backCard = ((AbstractTSCard) fromCard).thisCopy;
                }
            } else {
                card.backCard = fromCard;
            }
            return card;
        }
    }

    public static void cloneFieldToCard(AbstractCard ori, AbstractCard c) {
        ori.type = c.type;
        ori.rarity = c.rarity;
        ori.target = c.target;
        ReflectionHacks.setPrivate(ori, AbstractCard.class, "isMultiDamage", ReflectionHacks.getPrivate(c, AbstractCard.class, "isMultiDamage"));

        ori.isInnate = c.isInnate;
        ori.selfRetain = c.selfRetain;
        ori.isEthereal = c.isEthereal;
        ori.exhaust = c.exhaust;

        ori.costForTurn = c.costForTurn;
        ori.cost = c.cost;
        ori.isCostModified = c.isCostModified;
        ori.isCostModifiedForTurn = c.isCostModifiedForTurn;

        ori.freeToPlayOnce = c.freeToPlayOnce;
        ori.timesUpgraded = c.timesUpgraded;

        if (ori instanceof AbstractTSCard) {
            if (((AbstractTSCard) ori).thisCopy instanceof AbstractTSCard) {
                ((AbstractTSCard) ((AbstractTSCard) ori).thisCopy).cloneFieldCommon(ori);
            }

            if (((AbstractTSCard) ori).backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) ((AbstractTSCard) ori).backCard).cloneFieldCommon(ori);
            }
        }

    }

    public static void onFlip(AbstractTSCard card) {
//        触发时为翻转后
        if (card.isFlip) {
            if (card.thisCopy instanceof AbstractTSCard)
                ((AbstractTSCard) card.thisCopy).onFlip(card, false);
        } else {
            card.onFlipInHand(card, true);
        }

        if (card.backCard instanceof AbstractTSCard)
            ((AbstractTSCard) card.backCard).onFlip(card, card.isFlip);

    }

    public static void onFlipInHand(AbstractTSCard card) {
//        触发时为翻转后
        if (card.isFlip) {
            if (card.thisCopy instanceof AbstractTSCard)
                ((AbstractTSCard) card.thisCopy).onFlipInHand(card, false);
        } else {
            card.onFlipInHand(card, true);
        }

        if (card.backCard instanceof AbstractTSCard)
            ((AbstractTSCard) card.backCard).onFlipInHand(card, card.isFlip);

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

            if (card.canDoubleTrigger()) {
                if (card.backCard == null) {
                    if (card.thisCopy != null && card.thisCopy.target == AbstractCard.CardTarget.ENEMY)
                        return hoverCard.target;
                }

                if (card.thisCopy == null) {
                    if (card.backCard != null && card.backCard.target == AbstractCard.CardTarget.ENEMY)
                        return hoverCard.target;
                }

                if (card.backCard != null && card.thisCopy != null)
                    if (card.thisCopy.target == AbstractCard.CardTarget.ENEMY || card.backCard.target == AbstractCard.CardTarget.ENEMY)
                        return hoverCard.target;
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
            clz = AbstractPlayer.class,
            method = "updateCardsOnDamage"
    )
    public static class TookDamagePatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("tookDamage")) {
                        m.replace(BackCardManager.class.getName() + ".onTookDamage(c);"
                        );
                    }
                }
            };
        }
    }

    public static void onTookDamage(AbstractCard card) {
        if (card instanceof AbstractTSCard) {
            AbstractTSCard c = (AbstractTSCard) card;

            if (c.isFlip) {
                if (c.backCard != null && c.backCard != c) {
                    c.backCard.tookDamage();
                }
            } else {
                if (c.thisCopy != null && c.thisCopy != c) {
                    c.thisCopy.tookDamage();
                }
            }

            c.tookDamage();
        }

    }

}
