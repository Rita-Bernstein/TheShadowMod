package TheShadowMod.patches;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class Free2PlayOncePatches {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class Free2PlayOncePatch1 {
        @SpireInsertPatch(rloc = 100-84)
        public static void Insert(UseCardAction _inst, AbstractCard ___targetCard) {
            if (___targetCard instanceof AbstractTSCard) {
                AbstractTSCard c = (AbstractTSCard) ___targetCard;
                if (c.backCard != null) {
                    c.backCard.freeToPlayOnce = false;
                }
                if (c.thisCopy != null) {
                    c.thisCopy.freeToPlayOnce = false;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class Free2PlayOncePatch2 {
        @SpireInsertPatch(rloc = 2866-2858)
        public static SpireReturn<Boolean> Insert(AbstractCard _inst) {
            if (_inst instanceof AbstractTSCard) {
                AbstractTSCard c = (AbstractTSCard) _inst;
                if (c.backCard != null && c.backCard.freeToPlayOnce) {
                    return SpireReturn.Return(true);
                }
                if (c.thisCopy != null && c.thisCopy.freeToPlayOnce) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
