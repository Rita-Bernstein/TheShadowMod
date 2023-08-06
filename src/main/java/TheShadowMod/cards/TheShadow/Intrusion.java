package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Intrusion extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Intrusion.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Intrusion.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Intrusion() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 9;
        this.magicNumber = this.baseMagicNumber = 1;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DrawCardAction(1));
        addToBot(new GainFlipPowerAction(this.magicNumber));
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}
