package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.powers.TheShadow.HeavyPower;
import TheShadowMod.powers.TheShadow.PealPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SonicBoom extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(SonicBoom.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/SonicBoom.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SonicBoom() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
        this.secondaryM = this.baseSecondaryM = 50;

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPealPowerAction(m,this.magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if(m.hasPower(PealPower.POWER_ID)  && m.getPower(PealPower.POWER_ID).amount > this.startDuration){
                    addToTop(new GainEnergyAction(1));
                    addToTop(new DrawCardAction(2));
                }
                isDone= true;
            }
        });
    }

    @Override
    public void applyPowers() {
        this.magicNumber = this.baseMagicNumber;
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(HeavyPower.POWER_ID))
            this.magicNumber += AbstractDungeon.player.getPower(HeavyPower.POWER_ID).amount * (this.upgraded ? 3 : 2);
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeSecondM(-10);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
