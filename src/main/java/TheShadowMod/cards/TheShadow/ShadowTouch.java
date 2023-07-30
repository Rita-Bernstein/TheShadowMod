package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadowTouch extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(ShadowTouch.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/ShadowTouch.png");
    private static final int COST = 3;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ShadowTouch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 16;


    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void onFlip() {
        super.onFlip();
        updateCost(-1);
        this.thisCopy.updateCost(-1);
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(6);
        }
    }
}
