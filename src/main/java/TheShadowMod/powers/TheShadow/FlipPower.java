package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.FlipCombatCardsAction;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FlipPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(FlipPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public FlipPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("FlipPower");
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount = stackAmount;
    }

    @Override
    public void onRemove() {
        addToBot(new FlipCombatCardsAction());
    }


    @Override
    public void onPlayCard(AbstractCard usedCard, AbstractMonster m) {
        this.amount--;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}