package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class AnnihilatePower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(AnnihilatePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public AnnihilatePower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount= amount;

        updateDescription();

        loadShadowRegion("PealPower");
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        addToTop(new ReducePowerAction(this.owner, this.owner, POWER_ID,1));
    }

    @Override
    public void updateDescription() {
        this.description =this.amount>1? String.format( DESCRIPTIONS[1],this.amount):DESCRIPTIONS[0];
    }
}