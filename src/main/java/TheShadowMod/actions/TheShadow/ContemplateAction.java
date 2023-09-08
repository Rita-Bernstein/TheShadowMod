package TheShadowMod.actions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class ContemplateAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheShadowMod.makeID(ContemplateAction.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;

    private CurrentScreen currentScreen;
    private boolean firstUse = true;

    private AbstractCard saveSourceCard;

    public ContemplateAction() {

        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;

        currentScreen = CurrentScreen.Source;
    }

    private enum CurrentScreen {
        Source, Target,
    }


    public void update() {
        if (AbstractDungeon.player.hand.isEmpty() || AbstractDungeon.player.drawPile.group.isEmpty()) {
            this.isDone = true;
            return;
        }


        if (this.currentScreen == CurrentScreen.Source) {
            if (firstUse) {
                this.firstUse = false;

                if (AbstractDungeon.player.hand.group.size() == 1) {
                    saveSourceCard = AbstractDungeon.player.hand.group.get(0);
                    AbstractDungeon.player.hand.group.remove(saveSourceCard);
                    this.currentScreen = CurrentScreen.Target;
                    this.firstUse = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, true, true);
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                if (AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    isDone = true;
                    return;
                }
                saveSourceCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                this.currentScreen = CurrentScreen.Target;
                this.firstUse = true;

                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
        }

        if (this.currentScreen == CurrentScreen.Target) {
            if (firstUse) {
                this.firstUse = false;

                if (AbstractDungeon.player.drawPile.group.size() == 1) {

                    AbstractTSCard t = BackCardManager.setCardToBackCard(saveSourceCard, AbstractDungeon.player.drawPile.group.get(0), true);

                    AbstractDungeon.player.drawPile.group.set(0, t);


                    AbstractDungeon.player.hand.refreshHandLayout();

                    addToTop(new DrawCardAction(1));

                    isDone = true;
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.drawPile, 1, TEXT[1], false,
                        false, false, false);
                return;
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

                AbstractCard t = BackCardManager.setCardToBackCard(saveSourceCard, AbstractDungeon.gridSelectScreen.selectedCards.get(0), true);

                AbstractDungeon.player.drawPile.group.set(AbstractDungeon.player.drawPile.group.indexOf(t), t);

                addToTop(new DrawCardAction(1));

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
                this.isDone = true;
            }
        }
    }
}


