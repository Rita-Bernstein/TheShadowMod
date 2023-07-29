package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class EternalPunishmentPower2 extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(EternalPunishmentPower2.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;



    public EternalPunishmentPower2(AbstractCreature owner, int amount, int damage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.amount2 = damage;
        this.owner = owner;
        updateDescription();

        loadShadowRegion("PealPower");
    }


    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        for (int i = 0; i < this.amount; i++)
            addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, this.amount2, DamageInfo.DamageType.THORNS), true));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount2,this.amount);
    }
}