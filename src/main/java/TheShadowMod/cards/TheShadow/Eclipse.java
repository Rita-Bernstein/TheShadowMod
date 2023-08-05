package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;

public class Eclipse extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Eclipse.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Eclipse.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Eclipse() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 20;
        this.isEthereal = true;
        this.isInnate = true;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p,p,new InvinciblePower(p,this.magicNumber){
            @Override
            public void atEndOfRound() {
                addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,InvinciblePower.POWER_ID));
            }
        }));
    }

    @Override
    public void onInitializeBackCard(AbstractCard thisCard) {
        thisCard.isInnate = true;
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isEthereal = false;
            this.isInnate = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
