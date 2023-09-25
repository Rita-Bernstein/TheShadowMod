package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.helpers.BackCardManager;
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
    private static final CardTarget TARGET = CardTarget.NONE;

    public Fluctuate() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = 9;

    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new SelectHandCardAction(EXTENDED_DESCRIPTION[0], 1, cards -> {
            for (AbstractCard c : cards) {
                AbstractCard back = BackCardManager.AddFields.backCard.get(c);
                if (back != null && back != c) {
                    BackCardManager.AddFields.flipOnUseOnce.set(back, true);
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(back, m,back.energyOnUse,true,true));
                }else {
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, m,c.energyOnUse,true,true));
                }
            }
        }));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(4);
        }
    }
}
