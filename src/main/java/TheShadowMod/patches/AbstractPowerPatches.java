package TheShadowMod.patches;

import TheShadowMod.helpers.ShadowImageMaster;
import TheShadowMod.powers.AbstractShadowModPower;
import TheShadowMod.powers.TheShadow.PealPower;
import TheShadowMod.powers.TheShadow.UltrasonicPower;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.CtBehavior;

public class AbstractPowerPatches {
    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "create"
    )
    public static class initializePatch {
        @SpireInsertPatch(locator = TabNameLocator.class)
        public static SpireReturn<Void> Insert() {
            AbstractShadowModPower.initialize();
            ShadowImageMaster.initialize();
            return SpireReturn.Continue();
        }


        private static class TabNameLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "initialize");
                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    //    免死成功patch
    @SpirePatch(
            cls = "com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnPlayerDeathPatch",
            method = "Insert"
    )
    public static class StSlibPlayerDeathPatchPower {
        @SpireInsertPatch(rloc = 3)
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractShadowModPower) {
                    ((AbstractShadowModPower) p).onNotDeath();
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            cls = "com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnPlayerDeathPatch",
            method = "Insert"
    )
    public static class StSlibPlayerDeathPatchRelic {
        @SpireInsertPatch(rloc = 11)
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractShadowModPower) {
                    ((AbstractShadowModPower) p).onNotDeath();
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
    public static class PlayerDeathPatchFairy {
        @SpireInsertPatch(rloc = 132)
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractShadowModPower) {
                    ((AbstractShadowModPower) p).onNotDeath();
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
    public static class PlayerDeathPatchLizard {
        @SpireInsertPatch(rloc = 142)
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractShadowModPower) {
                    ((AbstractShadowModPower) p).onNotDeath();
                }
            }
            return SpireReturn.Continue();
        }
    }

}
