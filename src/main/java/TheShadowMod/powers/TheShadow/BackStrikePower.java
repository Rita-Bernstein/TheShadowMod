package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class BackStrikePower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(BackStrikePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public BackStrikePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = AbstractPower.PowerType.DEBUFF;
        updateDescription();

        loadShadowRegion("PealPower");
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            addToTop(new RemoveSpecificPowerAction(this.owner,this.owner,POWER_ID));
            return (int) (damageAmount * 1.5f);
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}