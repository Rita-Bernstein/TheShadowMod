package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.ApplyPowerToAllEnemyAction;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class Shroud extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Shroud.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Shroud.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Shroud() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 6;
        this.isMultiDamage = true;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_HEAVY"));
        addToBot(new VFXAction(p, new CleaveEffect(), 0.0F));
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.NONE, true));

        addToBot(new ApplyPowerToAllEnemyAction((mo) ->addToTop(new ApplyPealPowerAction(mo,this.magicNumber))));
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }
}
