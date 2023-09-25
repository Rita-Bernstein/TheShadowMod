package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.TempIncreaseMaxHPAction;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SinkIntoDarkPower2 extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(SinkIntoDarkPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public SinkIntoDarkPower2(AbstractCreature owner, int amount, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.amount2 = amount2;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("SeparationPower");
    }


    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            if (GameStatsPatch.blackWorld) {
                flash();
                addToBot(new TempIncreaseMaxHPAction(AbstractDungeon.player, this.amount2));
            }
            addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = this.amount > 1 ? String.format(DESCRIPTIONS[1], this.amount, this.amount2) : String.format(DESCRIPTIONS[0], this.amount2);
    }
}