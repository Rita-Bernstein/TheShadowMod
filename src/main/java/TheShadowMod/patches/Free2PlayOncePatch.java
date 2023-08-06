package TheShadowMod.patches;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.relics.TheShadow.NotDeadRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(clz = UseCardAction.class, method = "update")
public class Free2PlayOncePatch {
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
