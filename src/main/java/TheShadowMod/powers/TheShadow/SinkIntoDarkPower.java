package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.TempIncreaseMaxHPAction;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SinkIntoDarkPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(SinkIntoDarkPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public SinkIntoDarkPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        this.priority = 40;
        updateDescription();

        loadShadowRegion("SeparationPower");
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 先既视感再遁入黑暗也可以触发
        if (isPlayer) {
            int am = this.amount;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (GameStatsPatch.blackWorld) {
                        flash();
                        addToTop(new TempIncreaseMaxHPAction(AbstractDungeon.player, am));
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void updateDescription() {
        this.description =  String.format(DESCRIPTIONS[0], this.amount);
    }
}