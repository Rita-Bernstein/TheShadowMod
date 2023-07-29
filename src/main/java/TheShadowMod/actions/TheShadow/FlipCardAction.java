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

    @Override
    public void update() {
        if( this.card == null || this.group == null) {
            isDone = true;
            return;
        }

        for (AbstractCard c : this.group.group) {
            if (c instanceof AbstractTSCard) {
                ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                if(this.group == AbstractDungeon.player.hand) {
                    ((AbstractTSCard) c).onFlip();
                    ((AbstractTSCard) c).onFlipInHand();
                }
            }
        }

        this.isDone = true;
    }
}
