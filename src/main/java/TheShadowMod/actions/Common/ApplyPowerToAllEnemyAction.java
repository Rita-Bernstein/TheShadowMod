package TheShadowMod.actions.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ApplyPowerToAllEnemyAction extends AbstractGameAction {
    private Supplier<AbstractPower> powerToApply = null;
    private Consumer<AbstractMonster> action = null;

    public ApplyPowerToAllEnemyAction(Supplier<AbstractPower> powerToApply) {
        this.powerToApply = powerToApply;
    }

    public ApplyPowerToAllEnemyAction(Consumer<AbstractMonster> action) {
        this.action = action;
    }

    @Override
    public void update() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    if (this.action != null) {
                        this.action.accept(monster);
                        continue;
                    }

                    AbstractPower power = powerToApply.get();
                    power.owner = monster;
                    addToTop(new ApplyPowerAction(monster, AbstractDungeon.player, power));
                }
            }
        }
        isDone = true;
    }
}
