package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DeepDiving extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DeepDiving.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DeepDiving.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DeepDiving() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 2;
        this.secondaryM = this.baseSecondaryM = 2;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new GainFlipPowerAction(this.secondaryM));

    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            upgradeSecondM(1);
        }
    }
}
