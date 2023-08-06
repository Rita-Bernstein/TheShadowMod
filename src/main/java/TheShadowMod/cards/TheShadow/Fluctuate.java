package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectHandCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fluctuate extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Fluctuate.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Fluctuate.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Fluctuate() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = 9;

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,p,this.block));
        addToBot(new SelectHandCardAction(EXTENDED_DESCRIPTION[0], 1, card -> card instanceof  AbstractTSCard,cards -> {
            for (AbstractCard c : cards) {
                if(c instanceof  AbstractTSCard) {
                    ((AbstractTSCard) c).isFlip = !((AbstractTSCard) c).isFlip;
                    ((AbstractTSCard) c).onFlip();
                    AbstractTSCard tsc = (AbstractTSCard) c;
                    c.freeToPlayOnce = true;
                    if (tsc.thisCopy != null) {
                        tsc.thisCopy.freeToPlayOnce = true;
                    }
                    if (tsc.backCard != null) {
                        tsc.backCard.freeToPlayOnce = true;
                    }
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, m));
                }
            }
        }));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(4);
        }
    }
}
