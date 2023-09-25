package TheShadowMod.actions.TheShadow;

import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.TheShadow.*;
import TheShadowMod.relics.TheShadow.Knell;
import TheShadowMod.relics.TheShadow.VocalCords;
import TheShadowMod.relics.TheShadow.Waterphone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ApplyPealPowerAction extends AbstractGameAction {
    private boolean isGrave;

    public ApplyPealPowerAction(AbstractCreature creature, int amount, boolean isGrave) {
        this.amount = amount;
        this.target = creature;
        this.actionType = ActionType.POWER;
        this.isGrave = isGrave;

    }

    public ApplyPealPowerAction(AbstractCreature creature, int amount) {
        this(creature, amount, false);

    }

    @Override
    public void update() {
        if (this.target == null) {
            this.target = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (this.target == null) {
                this.isDone = true;
                return;
            }
        }
        if (PealPower.getPealCounter(this.target) >= getMaxPealAmount()) {
            if (this.target.hasPower(PealPower.POWER_ID) && this.target.getPower(PealPower.POWER_ID).amount > 0) {

                addToBot(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player,
                        this.target.getPower(PealPower.POWER_ID).amount, DamageInfo.DamageType.THORNS)));
            }
            this.isDone = true;
            return;
        }

        if (AbstractDungeon.player.hasRelic(Knell.ID)) {
            AbstractDungeon.player.getRelic(Knell.ID).flash();

            addToTop(new DrawCardAction(3));
        }

        if (AbstractDungeon.player.hasPower(UltrasonicPower.POWER_ID)) {
            this.amount += AbstractDungeon.player.getPower(UltrasonicPower.POWER_ID).amount;
        }

        if (AbstractDungeon.player.hasRelic(Waterphone.ID)) {
            this.amount += 3;
        }
        if (AbstractDungeon.player.hasRelic(VocalCords.ID)) {
            AbstractRelic r = AbstractDungeon.player.getRelic(VocalCords.ID);
            if (!r.grayscale) {
                this.amount *= 2;
                r.grayscale = true;
            }
        }

        if (!this.target.hasPower(ArtifactPower.POWER_ID)) {
            PealPower.addPealCounter(this.target, 1);


//            if (AbstractDungeon.player.hasPower(GatheringDarknessPower.POWER_ID))
//                if (AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2 > 0)
//
//                    addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player,
//                            AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2));
        }

        if (AbstractDungeon.player.hasPower(GatheringDarknessPower.POWER_ID) && GameStatsPatch.blackWorld) {
            if (AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2 > 0)

                addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player,
                        AbstractDungeon.player.getPower(GatheringDarknessPower.POWER_ID).amount * this.amount / 2));
        }

        ifFatalPealDamage(this.target);
        addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new PealPower(target, this.amount)));


        isDone = true;

    }

    public void ifFatalPealDamage(AbstractCreature target) {
        if (target.hasPower(PealPower.POWER_ID) && target.getPower(PealPower.POWER_ID).amount > 0) {
            if (this.isGrave)
                addToTop(new PealDamageAction(target, new DamageInfo(AbstractDungeon.player, target.getPower(PealPower.POWER_ID).amount, DamageInfo.DamageType.THORNS)));
            else
                addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, target.getPower(PealPower.POWER_ID).amount, DamageInfo.DamageType.THORNS)));
        }
    }

    public static int getMaxPealAmount() {
        if (AbstractDungeon.player.hasPower(EchoPower.POWER_ID)) {
            return 1 + AbstractDungeon.player.getPower(EchoPower.POWER_ID).amount;
        }
        return 1;
    }
}
