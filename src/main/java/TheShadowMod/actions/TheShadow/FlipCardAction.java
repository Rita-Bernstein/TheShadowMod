package TheShadowMod.actions.TheShadow;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FlipCardAction extends AbstractGameAction {
    private AbstractCard card;
    private CardGroup group;

    public FlipCardAction(AbstractCard card, CardGroup group) {
        this.actionType = ActionType.SPECIAL;
        this.card = card;
        this.group = group;
    }

    public FlipCardAction(AbstractCard card) {
        this.actionType = ActionType.SPECIAL;
        this.card = card;
        this.group = null;
    }


    @Override
    public void update() {
        if (this.card == null) {
            isDone = true;
            return;
        }

        if(this.group != null) {
            for (AbstractCard c : this.group.group) {
                if (c instanceof AbstractTSCard) {
                    ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                    if (this.group == AbstractDungeon.player.hand) {
                        ((AbstractTSCard) c).onFlip();
                        ((AbstractTSCard) c).onFlipInHand();
                    }
                }
            }
        }else {

            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c == this.card) {
                    ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                    ((AbstractTSCard) c).onFlip();
                    ((AbstractTSCard) c).onFlipInHand();
                }
            }

            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c == this.card) {
                    ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                    ((AbstractTSCard) c).onFlip();
                }
            }

            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c == this.card) {
                    ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                    ((AbstractTSCard) c).onFlip();
                }
            }

        }
        this.isDone = true;
    }
}
