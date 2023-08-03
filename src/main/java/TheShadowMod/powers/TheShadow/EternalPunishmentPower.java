package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EternalPunishmentPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(EternalPunishmentPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;



    public EternalPunishmentPower(AbstractCreature owner, int amount, int damage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.amount2 = damage;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("EternalPunishmentPower");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (int i = 0; i < this.amount; i++)
            addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, this.amount2, DamageInfo.DamageType.THORNS), true));
    }


    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount2,this.amount);
    }
}