package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.powers.AbstractShadowModPower;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class SeparationPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(SeparationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public SeparationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("PealPower");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
    }


    @SpirePatch(
            clz = GainBlockAction.class,
            method = "update"
    )
    public static class PatchRender {
        @SpireInsertPatch(rloc = 3)
        public static void Insert(GainBlockAction _instance) {
            if (AbstractDungeon.player.hasPower(SeparationPower.POWER_ID) && _instance.target != null && _instance.target.isPlayer)
                _instance.amount *= 2;
        }
    }


    @Override
    public void updateDescription() {
        this.description = this.amount > 1 ? String.format(DESCRIPTIONS[1], this.amount) : DESCRIPTIONS[0];
    }
}