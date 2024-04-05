package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.actions.TheShadow.FlipCardAction;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Airflow extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Airflow.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Airflow.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Airflow() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 2;
        this.secondaryM = this.baseSecondaryM = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new SelectHandCardAction(true,String.format(EXTENDED_DESCRIPTION[0], this.secondaryM), this.secondaryM, cards -> {
            for (AbstractCard c : cards) {
                BackCardManager.flipSameSideBackgroundView(c);
                AbstractCard b = BackCardManager.flipCard(c);

                AbstractDungeon.player.hand.addToTop(b);

                if (c instanceof AbstractTSCard) {
                    BackCardManager.onFlip((AbstractTSCard) c);
                    BackCardManager.onFlipInHand((AbstractTSCard) c);
                }
            }

        }));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            upgradeSecondM(1);
        }
    }


}
