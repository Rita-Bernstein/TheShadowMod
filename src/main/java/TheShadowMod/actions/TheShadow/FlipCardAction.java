package TheShadowMod.actions.TheShadow;

import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
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

        if (this.group != null) {

            for (int i = 0; i < group.group.size(); i++) {
                AbstractCard c = group.group.get(i);
                BackCardManager.flipSameSideBackgroundView(c);
                group.group.set(i, BackCardManager.flipCard(c));

                if (c instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) c);

                    if (this.group == AbstractDungeon.player.hand)
                        BackCardManager.onFlipInHand((AbstractTSCard) c);
                }
            }

        } else {
            for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.hand.group.get(i);

                if (c == this.card) {
                    BackCardManager.flipSameSideBackgroundView(c);
                    AbstractDungeon.player.hand.group.set(i, BackCardManager.flipCard(c));

                    if (c instanceof AbstractTSCard) {
                        BackCardManager.onFlip((AbstractTSCard) c);
                        BackCardManager.onFlipInHand((AbstractTSCard) c);
                    }
                }
            }


            for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.drawPile.group.get(i);
                if (c == this.card) {
                    BackCardManager.flipSameSideBackgroundView(c);
                    AbstractDungeon.player.drawPile.group.set(i, BackCardManager.flipCard(c));

                    if (c instanceof AbstractTSCard) {
                        BackCardManager.onFlip((AbstractTSCard) c);
                    }
                }
            }

            for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i++) {
                AbstractCard c = AbstractDungeon.player.discardPile.group.get(i);
                if (c == this.card) {
                    BackCardManager.flipSameSideBackgroundView(c);
                    AbstractDungeon.player.discardPile.group.set(i, BackCardManager.flipCard(c));

                    if (c instanceof AbstractTSCard) {
                        BackCardManager.onFlip((AbstractTSCard) c);
                    }
                }
            }

        }
        this.isDone = true;
    }
}
