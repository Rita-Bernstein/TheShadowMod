package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DoubleBody extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DoubleBody.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DoubleBody.png");
    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DoubleBody() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.isEthereal = true;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:CannotPlay").TEXT[0];
        return false;
    }

    @Override
    public void onFlipInHand(AbstractTSCard thisCard, boolean flipThisSide) {
        if (!flipThisSide)
            return;

        addToTop(new AbstractGameAction() {
                     @Override
                     public void update() {
                         AbstractMonster m = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.miscRng);
                         ArrayList<AbstractCard> list = new ArrayList<>(AbstractDungeon.player.hand.group);
                         list.removeIf(card -> card == DoubleBody.this || (card == BackCardManager.AddFields.backCard.get(DoubleBody.this) || card.canUse(AbstractDungeon.player,m)));

                         AbstractCard c;
                         if (list.size() > 1)
                             c = list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
                         else
                             c = list.get(0);

                         c.freeToPlayOnce = true;
                         AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, m));
                         isDone = true;
                     }
                 }
        );

    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isEthereal = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
