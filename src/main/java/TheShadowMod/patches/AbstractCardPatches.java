package TheShadowMod.patches;

import TheShadowMod.cards.TheShadow.Bone;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.powers.TheShadow.PealPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class AbstractCardPatches {
    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class InnateCardPatch extends ExprEditor {
        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            if (f.getFieldName().equals("isInnate") && f.getClassName().equals(AbstractCard.class.getName())) {
                f.replace("{c.isInnate || " + BackCardManager.class.getName() + ".AddFields.backCard.get(c).isInnate;");
            }
        }
    }

    @SpirePatch(
            clz = EquilibriumPower.class,
            method = "atEndOfTurn"
    )
    public static class EtherealCardPatch extends ExprEditor {
        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                f.replace("{c.isEthereal || " + BackCardManager.class.getName() + ".AddFields.backCard.get(c).isEthereal;");
            }
        }
    }

    @SpirePatch(
            clz = ExhaustAllEtherealAction.class,
            method = "update"
    )
    public static class EtherealCardPatch2 extends ExprEditor {
        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                f.replace("{c.isEthereal || " + BackCardManager.class.getName() + ".AddFields.backCard.get(c).isEthereal;");
            }
        }
    }
    @SpirePatch(
            clz = RetainCardsAction.class,
            method = "update"
    )
    public static class EtherealCardPatch3 extends ExprEditor {
        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            if (f.getFieldName().equals("isEthereal") && f.getClassName().equals(AbstractCard.class.getName())) {
                f.replace("{c.isEthereal || " + BackCardManager.class.getName() + ".AddFields.backCard.get(c).isEthereal;");
            }
        }
    }
}
