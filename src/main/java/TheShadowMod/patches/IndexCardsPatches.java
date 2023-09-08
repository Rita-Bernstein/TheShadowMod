package TheShadowMod.patches;

import TheShadowMod.helpers.SaveHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IndexCardsPatches {
    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class AddFields {
        public static SpireField<Integer> cardIndex = new SpireField<>(() -> 0);
    }

    public static int getCardIndex(AbstractCard card) {
        return AddFields.cardIndex.get(card);
    }

    public static void setCardIndex(AbstractCard card, int amount) {
        AddFields.cardIndex.set(card, amount);
    }

    public static void addCardIndex(AbstractCard card, int amount) {
        AddFields.cardIndex.set(card, AddFields.cardIndex.get(card) + amount);
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class OnVictoryPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            SaveHelper.saveCardOrderIndex();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "loadPlayerSave"
    )
    public static class PatchLoadPlayer {
        @SpireInsertPatch(rloc = 68)
        public static SpireReturn<Void> Insert(CardCrawlGame _instance, AbstractPlayer p) {
            SaveHelper.loadCardOrderIndex(p);

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class PatchCardMakeCopy {
        @SpireInsertPatch(rloc = 1, localvars = {"card"})
        public static SpireReturn<AbstractCard> Insert(AbstractCard _instance, @ByRef AbstractCard[] card) {
            IndexCardsPatches.setCardIndex(card[0], IndexCardsPatches.getCardIndex(_instance));

            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatPreDrawLogic"
    )
    public static class PatchPreBattlePrep {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            if (AbstractDungeon.player != null && !AbstractDungeon.player.drawPile.group.isEmpty()) {
                ArrayList<AbstractCard> cardsToIndex = new ArrayList<>();
                for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                    if (IndexCardsPatches.getCardIndex(card) > 0) {
                        cardsToIndex.add(card);
                    }
                }

                if (!cardsToIndex.isEmpty()) {
                    AbstractDungeon.player.drawPile.group.removeAll(cardsToIndex);
                    cardsToIndex.sort(Comparator.comparing(c -> IndexCardsPatches.getCardIndex(c)));

                    for (AbstractCard c : cardsToIndex) {
                        AbstractDungeon.player.drawPile.addToBottom(c);
                    }
                }

            }
            return SpireReturn.Continue();
        }
    }
}
