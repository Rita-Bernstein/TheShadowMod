package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.Execute;
import TheShadowMod.cards.TheShadow.Wallcovering;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class PeelPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(PeelPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public PeelPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        this.priority = 12;
        updateDescription();

        loadShadowRegion("PeelPower");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (int i = 0; i < this.amount; i++) {
            addToTop(new MakeTempCardInHandAction(new Execute()));
        }
        addToTop(new RemoveSpecificPowerAction(this.owner,this.owner,POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}