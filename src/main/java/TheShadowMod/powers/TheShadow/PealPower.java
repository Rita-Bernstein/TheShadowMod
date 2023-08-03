package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.AbstractShadowModPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class PealPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(PealPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public PealPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        updateDescription();
        this.type = PowerType.DEBUFF;
        loadShadowRegion("PealPower");
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = SpirePatch.CLASS
    )
    public static class AddFields {
        public static SpireField<Integer> pealCanApplyAmount = new SpireField<>(() -> 0);
    }

    public static int getPealCounter(AbstractCreature creature) {
        return AddFields.pealCanApplyAmount.get(creature);
    }

    public static void setPealCounter(AbstractCreature creature,int amount) {
        AddFields.pealCanApplyAmount.set(creature,amount);
    }
    public static void addPealCounter(AbstractCreature creature,int amount) {
        AddFields.pealCanApplyAmount.set(creature,AddFields.pealCanApplyAmount.get(creature)+amount);
    }

}