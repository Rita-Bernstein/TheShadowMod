package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class ShadowTouch extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(ShadowTouch.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/ShadowTouch.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public ShadowTouch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 2;
        this.isMultiDamage = true;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new VFXAction(p, new CleaveEffect(), 0.0F));
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.NONE, true));
    }

    @Override
    public void useCommon(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new VFXAction(p, new CleaveEffect(), 0.0F));
        addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(this.magicNumber),
                this.damageType, AbstractGameAction.AttackEffect.NONE, true));
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeMagicNumber(2);
        }
    }
}
