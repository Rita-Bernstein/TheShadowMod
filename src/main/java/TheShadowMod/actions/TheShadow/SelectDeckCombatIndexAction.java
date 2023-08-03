package TheShadowMod.actions.TheShadow;

import TheShadowMod.patches.AbstractCardPatches;
import TheShadowMod.patches.IndexCardsPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class SelectDeckCombatIndexAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("TheShadowMod:SelectDeckCombatBottomAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;


    public SelectDeckCombatIndexAction() {
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.25F;
        this.p = AbstractDungeon.player;
    }


    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                temp.addToTop(card);
            }

            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            }

            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);


            AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], false, false, false, false);
        }


        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            int maxIndex = 0;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                maxIndex = Math.max(maxIndex,  IndexCardsPatches.getCardIndex(c));
            }


            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (IndexCardsPatches.getCardIndex(card) > 0) {
                    for(AbstractCard tmp : AbstractDungeon.player.masterDeck.group){
                        IndexCardsPatches.addCardIndex(card,-1);
                    }
                    IndexCardsPatches.setCardIndex(card, maxIndex);
                }else {
                    maxIndex++;
                    IndexCardsPatches.setCardIndex(card, maxIndex);
                }
            }




        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();


        tickDuration();
    }
}
