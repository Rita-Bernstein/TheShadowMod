package TheShadowMod.actions.TheShadow;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.patches.FlipCardEffectPatches;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SwitchWorldAction extends AbstractGameAction {
    private boolean flipped = false;

    public SwitchWorldAction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = 0.25f;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        if (!this.flipped) {
            FlipCardEffectPatches.cardScaleX = this.duration / this.startDuration;
            FlipCardEffectPatches.flipBgOffset += Gdx.graphics.getDeltaTime() / this.startDuration;
        } else {
            FlipCardEffectPatches.cardScaleX = (this.startDuration - this.duration) / this.startDuration;
        }


        if (!this.flipped && this.duration < 0.0f) {
            this.flipped = true;
            this.duration = startDuration;

            GameStatsPatch.blackWorld = !GameStatsPatch.blackWorld;

            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractShadowModPower)
                    ((AbstractShadowModPower) p).onSwitchWorld();
            }


            if (GameStatsPatch.blackWorld)
                FlipCardEffectPatches.flipBgOffset = 0.0f;
            else
                FlipCardEffectPatches.flipBgOffset = -1.0f;

            if (AbstractDungeon.player.cardInUse != null) {
                BackCardManager.flipSameSideBackgroundView(AbstractDungeon.player.cardInUse);
                AbstractDungeon.player.cardInUse = BackCardManager.flipCard(AbstractDungeon.player.cardInUse);

                if (AbstractDungeon.player.cardInUse instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) AbstractDungeon.player.cardInUse);
                }
            }

            for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.hand.group.get(i);
                BackCardManager.flipSameSideBackgroundView(c);
                AbstractDungeon.player.hand.group.set(i, BackCardManager.flipCard(c));

                if (c instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) c);
                    BackCardManager.onFlipInHand((AbstractTSCard) c);
                }
            }


            for (int i = 0; i < AbstractDungeon.actionManager.cardQueue.size(); i++) {
                AbstractDungeon.actionManager.cardQueue.get(i).card = BackCardManager.flipCard(
                        AbstractDungeon.actionManager.cardQueue.get(i).card);
            }


            for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.drawPile.group.get(i);
                BackCardManager.flipSameSideBackgroundView(c);
                AbstractDungeon.player.drawPile.group.set(i, BackCardManager.flipCard(c));

                if (c instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) c);
                }
            }

            for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.discardPile.group.get(i);
                BackCardManager.flipSameSideBackgroundView(c);
                AbstractDungeon.player.discardPile.group.set(i, BackCardManager.flipCard(c));

                if (c instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) c);
                }
            }
        }


        if (this.flipped && this.duration < 0.0f) {
            FlipCardEffectPatches.cardScaleX = 1.0f;
            this.isDone = true;
        }


    }
}
