package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.DoublePlayDrawPileAction;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Field;

public class UltimateReaction extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(UltimateReaction.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/UltimateReaction.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public UltimateReaction() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        boolean isMultiDmg = ReflectionHacks.getPrivate(m,AbstractMonster.class,"isMultiDmg");

        if(isMultiDmg){
            int intentMultiAmt = ReflectionHacks.getPrivate(m,AbstractMonster.class,"intentMultiAmt");

            if (m.getIntentDmg() * intentMultiAmt >= p.currentHealth) {
                addToBot(new DoublePlayDrawPileAction());
            }
        }else {
            if (m.getIntentDmg() >= p.currentHealth) {
                addToBot(new DoublePlayDrawPileAction());
            }
        }
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
