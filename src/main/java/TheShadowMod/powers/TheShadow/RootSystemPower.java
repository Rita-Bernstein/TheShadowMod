package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class RootSystemPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(RootSystemPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public RootSystemPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("FlipPower");
    }

    @Override
    public float modifyBlock(float blockAmount) {
        if (HeavyPower.canHeavyTrigger() && AbstractDungeon.player.hasPower(HeavyPower.POWER_ID)) {
            return blockAmount + AbstractDungeon.player.getPower(HeavyPower.POWER_ID).amount * this.amount;
        }
        return super.modifyBlock(blockAmount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}