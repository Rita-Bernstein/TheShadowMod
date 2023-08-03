package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UnmarkedCommonGraves extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(UnmarkedCommonGraves.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/UnmarkedCommonGraves.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public UnmarkedCommonGraves() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 11;
        this.exhaust = true;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPealPowerAction(m,this.magicNumber));
//        todo
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }
}
