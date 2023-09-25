package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.NextTurnPealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EmptyShell extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(EmptyShell.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/EmptyShell.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public EmptyShell() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = 8;
        this.magicNumber = this.baseMagicNumber = 8;

    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(m,p,new NextTurnPealPower(m,this.magicNumber)));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(3);
            upgradeMagicNumber(3);
        }
    }
}
