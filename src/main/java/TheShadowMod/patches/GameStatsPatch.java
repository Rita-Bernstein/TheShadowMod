package TheShadowMod.patches;

import TheShadowMod.helpers.SaveHelper;
import TheShadowMod.powers.TheShadow.PealPower;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue.Save;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom;

public class GameStatsPatch {
    public static boolean blackWorld = false;
    public static int notDeathCombatCounter = 0;
    public static int trueDamageReceivedThisCombat = 0;

    //    ========================================重置
    public static void turnBaseReset() {
        PealPower.setPealCounter(AbstractDungeon.player, 0);
    }

    public static void combatBaseReset() {
        blackWorld = false;
        FlipCardEffectPatches.flipBgOffset = -1.0f;

        notDeathCombatCounter = 0;
        trueDamageReceivedThisCombat = 0;

    }

    @SpirePatch(
            
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatLogic"
    )
    public static class NoPotionResetPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance) {
            SaveHelper.noPotion = false;
            SaveHelper.saveNoPotion();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PreBattlePrepPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            combatBaseReset();
            turnBaseReset();
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
            combatBaseReset();
            turnBaseReset();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "resetPlayer"
    )
    public static class ResetPlayerPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert() {
            combatBaseReset();
            turnBaseReset();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GetNextActionPatch {
        @SpireInsertPatch(rloc = 245)
        public static SpireReturn<Void> Insert(GameActionManager _instance) {
            turnBaseReset();

            for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    PealPower.setPealCounter(monster, 0);
                }
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = {DamageInfo.class}
    )
    public static class PlayerDamagePatch {
        @SpireInsertPatch(rloc = 62)
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            GameStatsPatch.trueDamageReceivedThisCombat += __instance.lastDamageTaken;
            return SpireReturn.Continue();
        }
    }

}
