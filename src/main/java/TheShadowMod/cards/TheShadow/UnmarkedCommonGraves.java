package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class UnmarkedCommonGraves extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(UnmarkedCommonGraves.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/UnmarkedCommonGraves.png");
    private static final int COST = 3;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public UnmarkedCommonGraves() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 7;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        int d = damage;
        DamageInfo.DamageType dt = damageTypeForTurn;
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    ArrayList<AbstractCreature> arrayList = new ArrayList<>();
                    arrayList.add(p);
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                        if (!mo.halfDead && !mo.isDying && !mo.isEscaping)
                            arrayList.add(mo);
                    }
                    AbstractCreature target = arrayList.get(AbstractDungeon.cardRandomRng.random(
                            0, arrayList.size() - 1));
                    addToTop(new DamageAction(target, new DamageInfo(p, d, dt),
                            AttackEffect.SLASH_VERTICAL));
                    isDone = true;
                }
            });
        }
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(1);
            upgradeMagicNumber(1);
        }
    }
}
