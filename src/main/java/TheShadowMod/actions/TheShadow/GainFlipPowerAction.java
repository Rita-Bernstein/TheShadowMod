package TheShadowMod.actions.TheShadow;

import TheShadowMod.powers.TheShadow.FlipPower;
import TheShadowMod.powers.TheShadow.PeelPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GainFlipPowerAction extends AbstractGameAction {
    public GainFlipPowerAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlipPower(AbstractDungeon.player, this.amount), this.amount));
        addToTop(new FlipCombatCardsAction());

        isDone = true;

    }
}
