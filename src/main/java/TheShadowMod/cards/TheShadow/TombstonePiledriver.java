package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.NextTurnPealPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class TombstonePiledriver extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(TombstonePiledriver.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/TombstonePiledriver.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TombstonePiledriver() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 12;
        this.magicNumber = this.baseMagicNumber = 12;
        this.secondaryM = this.baseSecondaryM = 1;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m,p,new NextTurnPealPower(m,this.magicNumber)));
        addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, this.secondaryM, false)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeMagicNumber(4);
        }
    }
}
