package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.ApplyPowerToAllEnemyAction;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Tinnitus extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Tinnitus.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Tinnitus.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Tinnitus() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
        this.secondaryM = this.baseSecondaryM = 2;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerToAllEnemyAction((monster)->{
            addToTop(new ApplyPowerAction(monster,p,new LoseStrengthPower(monster,this.secondaryM)));
            addToTop(new ApplyPowerAction(monster,p,new StrengthPower(monster,this.secondaryM)));
addToTop(new ApplyPealPowerAction(m,this.magicNumber));


        }));

    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
            upgradeSecondM(1);
        }
    }
}
