package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.powers.TheShadow.NextTurnPealPower;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PolarNight extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(PolarNight.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/PolarNight.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public PolarNight() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 20;
        this.secondaryM = this.baseSecondaryM = 10;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPealPowerAction(m,this.magicNumber));
        addToBot(new ApplyPowerAction(m,p,new NextTurnPealPower(m,this.secondaryM)));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(4);
            upgradeSecondM(2);
        }
    }
}
