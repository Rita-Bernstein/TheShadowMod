package TheShadowMod.patches;

import TheShadowMod.relics.TheShadow.BrokenMirror;
import TheShadowMod.relics.TheShadow.MistyMirror;
import TheShadowMod.relics.TheShadow.NotDeadRelic;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
public class NotDeathPatch {
    @SpireInsertPatch(rloc = 1853-1725)
    public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
        for (AbstractRelic relic : __instance.relics) {
            if (relic instanceof NotDeadRelic &&
                    !((NotDeadRelic)relic).onDead(__instance, info)) {
                GameStatsPatch.notDeathCombatCounter++;
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }

}