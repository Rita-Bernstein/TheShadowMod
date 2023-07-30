package TheShadowMod.patches;

import TheShadowMod.powers.TheShadow.PealPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GameStatsPatch {
    public static boolean blackWorld = false;
    public static int notDeathCombatCounter = 0;

    //    ========================================重置
    public static void turnBaseReset() {
        PealPower.getPealCounter(AbstractDungeon.player).atStartOfTurn();;


        if(AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT)
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    PealPower.getPealCounter(monster).atStartOfTurn();
                }
            }
        }


    }

    public static void combatBaseReset() {
        blackWorld = false;
        notDeathCombatCounter = 0;
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
            return SpireReturn.Continue();
        }
    }

}
