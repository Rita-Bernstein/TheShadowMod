package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class HeavyPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(HeavyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public HeavyPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("HeavyPower");
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class DamagePatchPlayer {
        @SpireInsertPatch(rloc = 60, localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer _instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (canHeavyTrigger() && AbstractDungeon.player.hasPower(HeavyPower.POWER_ID)) {
                AbstractDungeon.player.getPower(HeavyPower.POWER_ID).flash();
                if (damageAmount[0] > 0)
                    damageAmount[0] += AbstractDungeon.player.getPower(HeavyPower.POWER_ID).amount;
            }

        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class DamagePatchMonster {
        @SpireInsertPatch(rloc = 58, localvars = {"damageAmount"})
        public static void Insert(AbstractMonster _instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (canHeavyTrigger() && AbstractDungeon.player.hasPower(HeavyPower.POWER_ID)) {
                AbstractDungeon.player.getPower(HeavyPower.POWER_ID).flash();
                if (damageAmount[0] > 0)
                    info.output += AbstractDungeon.player.getPower(HeavyPower.POWER_ID).amount;
            }

        }
    }

    public static boolean canHeavyTrigger() {
        return !GameStatsPatch.blackWorld || AbstractDungeon.player.hasPower(MourningPower.POWER_ID);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}