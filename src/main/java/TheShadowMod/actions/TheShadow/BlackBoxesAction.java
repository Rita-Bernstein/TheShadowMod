package TheShadowMod.actions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class BlackBoxesAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheShadowMod.makeID(BlackBoxesAction.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;


    private ArrayList<AbstractCard> cannotDuplicate = new ArrayList<>();
    private CurrentScreen currentScreen;
    private boolean firstUse = true;

    private AbstractCard saveSourceCard;

    public BlackBoxesAction() {

        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;

        currentScreen = CurrentScreen.Source;
    }

    private enum CurrentScreen {
        Source, Target,
    }


    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty() || AbstractDungeon.player.hand.group.isEmpty()) {
            this.isDone = true;
            return;
        }


        if (this.currentScreen == CurrentScreen.Source) {
            if (firstUse) {
                this.firstUse = false;

                if (AbstractDungeon.player.drawPile.group.size() == 1) {
                    saveSourceCard = AbstractDungeon.player.drawPile.group.get(0);
                    AbstractDungeon.player.drawPile.group.remove(saveSourceCard);
                    this.currentScreen = CurrentScreen.Target;
                    this.firstUse = true;
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.drawPile, 1, TEXT[0], false,
                        false, false, false);
                return;
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                saveSourceCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.player.drawPile.group.remove(saveSourceCard);
                this.currentScreen = CurrentScreen.Target;
                this.firstUse = true;

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }

        if (this.currentScreen == CurrentScreen.Target) {
            if (firstUse) {
                this.firstUse = false;

                if (AbstractDungeon.player.hand.group.size() == 1) {
                    AbstractCard t = BackCardManager.setCardToBackCard(saveSourceCard,  AbstractDungeon.player.hand.group.get(0), true);

                    t.superFlash();
                    AbstractDungeon.player.hand.refreshHandLayout();

                    isDone = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard t = BackCardManager.setCardToBackCard(saveSourceCard, AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0), true);

                t.superFlash();
                AbstractDungeon.player.hand.addToTop(t);

                AbstractDungeon.player.hand.refreshHandLayout();

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.isDone = true;
            }
        }
    }

}


