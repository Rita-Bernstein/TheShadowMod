package TheShadowMod.actions.TheShadow;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
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

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlip();
                ((AbstractTSCard) c).onFlipInHand();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlip();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.limbo.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                ((AbstractTSCard) c).onFlip();
            }
        }

        this.isDone = true;
    }
}
