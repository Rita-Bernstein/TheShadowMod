package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.TempIncreaseMaxHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SinkIntoDark extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(SinkIntoDark.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/SinkIntoDark.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SinkIntoDark() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 12;

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TempIncreaseMaxHPAction(p,this.magicNumber));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(6);
        }
    }
}
