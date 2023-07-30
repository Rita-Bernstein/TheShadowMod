package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.GainMaxHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LingeringDeath extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(LingeringDeath.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/LingeringDeath.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public LingeringDeath() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 2;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainMaxHPAction(p,this.magicNumber));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
