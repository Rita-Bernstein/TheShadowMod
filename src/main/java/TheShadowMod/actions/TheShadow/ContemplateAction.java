package TheShadowMod.actions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
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


    private ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();
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
        if (AbstractDungeon.player.hand.isEmpty() || AbstractDungeon.player.drawPile.group.stream().noneMatch(card -> card instanceof AbstractTSCard)) {
            this.isDone = true;
            return;
        }


        if(this.currentScreen == CurrentScreen.Source){
            if (firstUse) {
                this.firstUse = false;

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, true, true);
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                if(AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()){
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    isDone = true;
                    return;
                }
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

                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for(AbstractCard c : AbstractDungeon.player.drawPile.group){
                    if(c instanceof  AbstractTSCard){
                        temp.addToTop(c);
                    }
                }

                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);


                if (temp.group.size() == 1) {
                    AbstractTSCard t = (AbstractTSCard) temp.group.get(0);
                    t.backCard = saveSourceCard.makeStatEquivalentCopy();
                    if (t.backCard instanceof AbstractTSCard) {
                        ((AbstractTSCard) t.backCard).backCard = null;
                    }

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

                AbstractTSCard t = (AbstractTSCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                t.backCard = saveSourceCard.makeStatEquivalentCopy();
                if (t.backCard instanceof AbstractTSCard) {
                    ((AbstractTSCard) t.backCard).backCard = null;
                }

                addToTop(new DrawCardAction(1));

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
                this.isDone = true;
            }
        }
    }
}


