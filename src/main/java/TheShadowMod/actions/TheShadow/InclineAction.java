package TheShadowMod.actions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class InclineAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheShadowMod.makeID(InclineAction.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;


    private ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();
    private CurrentScreen currentScreen;
    private boolean firstUse = true;

    private AbstractCard saveSourceCard;

    public InclineAction() {

        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;

        currentScreen = CurrentScreen.Source;
    }

    private enum CurrentScreen {
        Source, Target,
    }


    public void update() {
        if (AbstractDungeon.player.hand.isEmpty() || AbstractDungeon.player.hand.group.stream().noneMatch(card -> card instanceof AbstractTSCard)) {
            this.isDone = true;
            return;
        }


        if(this.currentScreen == CurrentScreen.Source){
            if (firstUse) {
                this.firstUse = false;

                if (AbstractDungeon.player.hand.group.size() == 1) {
                    saveSourceCard = AbstractDungeon.player.hand.group.get(0);
                    this.currentScreen = CurrentScreen.Target;
                    this.firstUse = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                saveSourceCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                AbstractDungeon.player.hand.addToTop(saveSourceCard);
                this.currentScreen = CurrentScreen.Target;
                this.firstUse = true;

                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
        }

        if(this.currentScreen == CurrentScreen.Target){
            if (firstUse) {
                this.firstUse = false;

                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (!(c instanceof AbstractTSCard) || c == saveSourceCard) {
                        cannotDuplicate.add(c);
                    }
                }


                AbstractDungeon.player.hand.group.removeAll(cannotDuplicate);


                if (AbstractDungeon.player.hand.group.size() == 1) {
                    AbstractTSCard t = (AbstractTSCard) AbstractDungeon.player.hand.group.get(0);
                    t.backCard = saveSourceCard.makeStatEquivalentCopy();
                    if (t.backCard instanceof AbstractTSCard) {
                        ((AbstractTSCard) t.backCard).backCard = null;
                    }
                    returnCards();

                    t.superFlash();
                    AbstractDungeon.player.hand.refreshHandLayout();

                    isDone = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {

                AbstractTSCard t = (AbstractTSCard) AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                t.backCard = saveSourceCard.makeStatEquivalentCopy();
                if (t.backCard instanceof AbstractTSCard) {
                    ((AbstractTSCard) t.backCard).backCard = null;
                }
                t.superFlash();
                AbstractDungeon.player.hand.addToTop(t);

                returnCards();

                AbstractDungeon.player.hand.refreshHandLayout();

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.isDone = true;
            }
        }
    }

    private void returnCards() {
        for (AbstractCard c : this.cannotDuplicate) {
            AbstractDungeon.player.hand.addToTop(c);
        }

        AbstractDungeon.player.hand.refreshHandLayout();
    }
}


