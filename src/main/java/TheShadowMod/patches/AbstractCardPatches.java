package TheShadowMod.patches;

import TheShadowMod.cards.TheShadow.Bone;
import TheShadowMod.powers.TheShadow.PealPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class AbstractCardPatches {
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCopy",
            paramtypez = {String.class, int.class, int.class}
    )
    public static class EsteemMiscPatches {
        @SpireInsertPatch(rloc = 14, localvars = {"retVal"})
        public static SpireReturn<AbstractCard> Insert(String key, int upgradeTime, int misc,
                                                       @ByRef(type = "com.megacrit.cardcrawl.cards.AbstractCard") Object[] card) {
            AbstractCard retVal = (AbstractCard) card[0];
            if (misc != 0) {
                if (retVal.cardID.equals(Bone.ID)) {
                    retVal.baseBlock = misc;
                    retVal.applyPowers();
                    retVal.initializeDescription();
                }
            }

            return SpireReturn.Continue();
        }
    }




}
