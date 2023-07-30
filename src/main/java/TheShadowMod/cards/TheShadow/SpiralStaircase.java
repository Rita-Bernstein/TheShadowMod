package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpiralStaircase extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(SpiralStaircase.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/SpiralStaircase.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SpiralStaircase() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 6;
        this.secondaryM = this.baseSecondaryM = 1;
        this.exhaust = true;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, this.magicNumber));
        addToBot(new GainEnergyAction(upgraded ? 2 : 1));
        addToBot(new GainFlipPowerAction(this.secondaryM));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeSecondM(1);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
