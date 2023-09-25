package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.EternalPunishmentPower;
import TheShadowMod.powers.TheShadow.EternalPunishmentPower2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EternalPunishment extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(EternalPunishment.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/EternalPunishment.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public EternalPunishment() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 7;

    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (!upgraded)
            addToBot(new ApplyPowerAction(m, p, new EternalPunishmentPower(m, 1, this.magicNumber), 1));
        else {
            addToBot(new ApplyPowerAction(m, p, new EternalPunishmentPower2(m, 1, this.magicNumber), 1));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(3);
        }
    }
}
