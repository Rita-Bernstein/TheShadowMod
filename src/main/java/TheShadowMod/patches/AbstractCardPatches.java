package TheShadowMod.patches;

import TheShadowMod.cards.TheShadow.Bone;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.helpers.ViewFlipButton;
import TheShadowMod.powers.AbstractShadowModPower;
import TheShadowMod.powers.TheShadow.PealPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class AbstractCardPatches {
    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class InnateCardPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isInnate") && f.getClassName().equals(AbstractCard.class.getName())) {
                        f.replace("{$_ = ( $proceed($$) || " + AbstractCardPatches.class.getName() + ".isDoubleInnate(c));}");
                    }
                }
            };
        }
    }


    @SpirePatch(
            clz = EquilibriumPower.class,
            method = "atEndOfTurn"
    )
    public static class EtherealCardPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                        f.replace("{$_ = ( $proceed($$) || !" + AbstractCardPatches.class.getName() + ".isDoubleEthereal(c));}");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = ExhaustAllEtherealAction.class,
            method = "update"
    )
    public static class EtherealCardPatch2 {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                        f.replace("{$_ = ( $proceed($$) || " + AbstractCardPatches.class.getName() + ".isDoubleEthereal(c));}");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = RetainCardsAction.class,
            method = "update"
    )
    public static class EtherealCardPatch3 {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                        f.replace("{$_ = ( $proceed($$) || !" + AbstractCardPatches.class.getName() + ".isDoubleEthereal(c));}");
                    }
                }
            };
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "triggerOnEndOfPlayerTurn"
    )
    public static class EtherealCardPatch4 {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                        f.replace("{$_ = ( $proceed($$) || " + AbstractCardPatches.class.getName() + ".isDoubleEthereal(this));}");
                    }
                }
            };
        }
    }


    public static boolean isDoubleEthereal(AbstractCard c) {
        return c.hasTag(CardTagsEnum.DoubleEthereal) || BackCardManager.AddFields.backCard.get(c).hasTag(CardTagsEnum.DoubleEthereal);
    }

    public static boolean isDoubleInnate(AbstractCard c) {
        return c.hasTag(CardTagsEnum.DoubleInnate) || BackCardManager.AddFields.backCard.get(c).hasTag(CardTagsEnum.DoubleInnate);
    }


    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class UpgradeCardGirdLogic {
        @SpireInsertPatch(rloc = 146-83)
        public static SpireReturn<Void> Insert(GridCardSelectScreen __instance) {
            AbstractCard hoveredCard = ReflectionHacks.getPrivate(__instance,GridCardSelectScreen.class,"hoveredCard");
            if(hoveredCard != null) {
                if (AbstractDungeon.gridSelectScreen.forUpgrade && hoveredCard.upgraded) {
                    hoveredCard.hb.clickStarted = false;
                }
            }
            return SpireReturn.Continue();
        }
    }


    public static boolean notHoverWhenUpgraded(AbstractCard c){
        if(AbstractDungeon.gridSelectScreen.forUpgrade){
            return !c.upgraded;
        }else {
            return true;
        }
    }

}
