package TheShadowMod.actions.TheShadow;

import TheShadowMod.powers.TheShadow.*;
import TheShadowMod.relics.TheShadow.Knell;
import TheShadowMod.relics.TheShadow.VocalCords;
import TheShadowMod.relics.TheShadow.Waterphone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ApplyPealPowerAction extends AbstractGameAction {


    public ApplyPealPowerAction(AbstractCreature creature, int amount) {
        this.amount = amount;
        this.target = creature;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (PealPower.getPealCounter(this.target).pealAppliedCount >= getMaxPealAmount()) {
            isDone = true;
            return;
        }

        if(AbstractDungeon.player.hasRelic(Knell.ID)){
             AbstractDungeon.player.getRelic(Knell.ID).flash();

            addToTop(new DrawCardAction(3));
        }

        if (AbstractDungeon.player.hasPower(UltrasonicPower.POWER_ID)) {
            this.amount += AbstractDungeon.player.getPower(UltrasonicPower.POWER_ID).amount;
        }

        if(AbstractDungeon.player.hasRelic(Waterphone.ID)){
            this.amount +=3;
        }
        if(AbstractDungeon.player.hasRelic(VocalCords.ID)){
            AbstractRelic r = AbstractDungeon.player.getRelic(VocalCords.ID);
            if(!r.grayscale){
                r.grayscale = true;
            }

            this.amount *=2;
        }

        if (!this.target.hasPower(ArtifactPower.POWER_ID)) {
            PealPower.getPealCounter(this.target).pealAppliedCount++;
            if (AbstractDungeon.player.hasPower(GatheringDarknessPower.POWER_ID))
                if (AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2 > 0)
                    addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player,
                            AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2));
        }

        addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new PealPower(target, this.amount)));


        isDone = true;

    }

    public static int getMaxPealAmount() {
        if (AbstractDungeon.player.hasPower(EchoPower.POWER_ID)) {
            return 1 + AbstractDungeon.player.getPower(EchoPower.POWER_ID).amount;
        }
        return 1;
    }
}
