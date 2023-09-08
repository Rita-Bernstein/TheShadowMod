package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectCardToHandAction;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.helpers.SaveHelper;
import TheShadowMod.patches.GameStatsPatch;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue.Save;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class DominatingFuture extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DominatingFuture.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DominatingFuture.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DominatingFuture() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardToHandAction(getRewardCards(), false, false));
    }

    public ArrayList<AbstractCard> getRewardCards() {
        ArrayList<AbstractCard> retVal = SaveHelper.loadRewardCard();
        if (SaveHelper.rewardNewAct) {
            SaveHelper.rewardNewAct = false;
            retVal.clear();
            AbstractCard card = AbstractDungeon.getCard(CardRarity.RARE);

            int numCards = 3;
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                numCards = r.changeNumberOfCardsInReward(numCards);
            }

            if (ModHelper.isModEnabled("Binary")) {
                numCards--;
            }

            for (int i = 0; i < numCards; i++) {
                while (retVal.contains(card)) {
                    card = AbstractDungeon.getCard(CardRarity.RARE);
                }
                retVal.add(card);
            }

            ArrayList<AbstractCard> retVal2 = new ArrayList<AbstractCard>();
            for (AbstractCard c : retVal) {
                retVal2.add(c.makeCopy());
            }

            SaveHelper.SaveRewardCard(retVal2);
            return retVal2;
        } else {
            return retVal;
        }
    }


    @SpirePatch(
            clz = RewardItem.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {CardColor.class}
    )
    public static class BossCardRewardPatch {
        @SpireInsertPatch(rloc = 11)
        public static SpireReturn<Void> Insert(RewardItem _instance,CardColor colorType) {
            if ((AbstractDungeon.getCurrRoom()) instanceof MonsterRoomBoss && colorType != CardColor.COLORLESS) {
                ArrayList<AbstractCard> retVal = SaveHelper.loadRewardCard();
                if (!retVal.isEmpty()) {
                    _instance.cards = retVal;
                }
            }
            return SpireReturn.Continue();
        }
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
