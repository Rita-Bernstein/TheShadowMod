package TheShadowMod.actions.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DoublePowerAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final AbstractPower power;

    public DoublePowerAction(AbstractPower power) {
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.power = power;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST && this.p.hasPower(power.ID)) {
            power.amount = this.p.getPower(power.ID).amount;
            addToTop(new ApplyPowerAction(this.p, this.p, power));
        }

        tickDuration();
    }
}
