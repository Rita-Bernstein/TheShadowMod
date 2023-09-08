package TheShadowMod.actions.TheShadow;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.patches.GameStatsPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FlipCombatCardsAction extends AbstractGameAction {
    public FlipCombatCardsAction() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        GameStatsPatch.blackWorld = !GameStatsPatch.blackWorld;

        if (AbstractDungeon.player.cardInUse instanceof AbstractTSCard) {
            ((AbstractTSCard) AbstractDungeon.player.cardInUse).isFlip = !((AbstractTSCard) AbstractDungeon.player.cardInUse).isFlip;
            ((AbstractTSCard) AbstractDungeon.player.cardInUse).onFlipView();
            BackCardManager.onFlip((AbstractTSCard) AbstractDungeon.player.cardInUse);
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlipView();
                BackCardManager.onFlip((AbstractTSCard) c);
                BackCardManager.onFlipInHand(((AbstractTSCard) c));
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlipView();
                BackCardManager.onFlip((AbstractTSCard) c);
            }
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlipView();
                BackCardManager.onFlip((AbstractTSCard) c);
            }
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlipView();
                BackCardManager.onFlip((AbstractTSCard) c);
            }
        }

        for (AbstractCard c : AbstractDungeon.player.limbo.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlipView();
                BackCardManager.onFlip((AbstractTSCard) c);
            }
        }

        this.isDone = true;
    }
}
