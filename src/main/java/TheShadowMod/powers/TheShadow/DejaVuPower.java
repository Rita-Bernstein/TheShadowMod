package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DejaVuPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(DejaVuPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public DejaVuPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();
        this.priority = 20;

        loadShadowRegion("DejaVuPower");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        addToBot(new ApplyPowerAction(this.owner,this.owner,new HeavyPower(this.owner,this.amount)));
        addToBot(new GainFlipPowerAction(this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format( DESCRIPTIONS[0],this.amount);
    }
}