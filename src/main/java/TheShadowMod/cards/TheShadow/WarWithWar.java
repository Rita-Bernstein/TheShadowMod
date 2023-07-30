package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.GainMaxHPAction;
import TheShadowMod.actions.TheShadow.TempIncreaseMaxHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WarWithWar extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(WarWithWar.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/WarWithWar.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public WarWithWar() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 9;
        this.magicNumber = this.baseMagicNumber = 9;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p,p,this.magicNumber));
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new TempIncreaseMaxHPAction(p,this.magicNumber));
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }
}
