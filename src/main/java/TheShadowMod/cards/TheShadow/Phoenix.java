package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.patches.GameStatsPatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Phoenix extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Phoenix.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Phoenix.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Phoenix() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 4;

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void applyPowers() {
        if (this.upgraded) {
            this.baseDamage = 8 * (int)Math.pow(2, GameStatsPatch.notDeathCombatCounter);
        } else {
            this.baseDamage = 4 * (int)Math.pow(2, GameStatsPatch.notDeathCombatCounter);
        }
        super.applyPowers();
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }
//    public void calculateCardDamage(AbstractMonster mo) {
//        super.calculateCardDamage(mo);
//        this.damage *= (int) Math.pow(2, GameStatsPatch.notDeathCombatCounter);
//    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }
}
