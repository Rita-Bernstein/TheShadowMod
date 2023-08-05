package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectCardToHandAction;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.helpers.SaveHelper;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue.Save;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;

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
        if (retVal.isEmpty()) {
            AbstractCard card = AbstractDungeon.getCard(CardRarity.RARE);
            for (int numCards = 3, i = 0; i < numCards; i++) {
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

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
