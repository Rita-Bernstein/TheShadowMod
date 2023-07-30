package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TrueSight extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(TrueSight.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/TrueSight.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public TrueSight() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 8;
        this.exhaust = true;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(8);
        }
    }
}
