package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.ApplyPowerToAllEnemyAction;
import TheShadowMod.actions.Common.XActionAction;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DesperateAttempt extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DesperateAttempt.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DesperateAttempt.png");
    private static final int COST = -1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public DesperateAttempt() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XActionAction(effect -> {
            addToBot(new ApplyPealPowerAction(p,effect*this.magicNumber));

        }, this.freeToPlayOnce, this.energyOnUse));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }
}
