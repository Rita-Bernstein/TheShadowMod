package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.TempIncreaseMaxHPAction;
import TheShadowMod.powers.TheShadow.SinkIntoDarkPower;
import TheShadowMod.powers.TheShadow.SinkIntoDarkPower2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SinkIntoDark extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(SinkIntoDark.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/SinkIntoDark.png");
    private static final int COST = 2;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SinkIntoDark() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
        this.secondaryM = this.baseSecondaryM = 2;
        this.tags.add(CardTags.HEALING);
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        if(!this.upgraded){
            addToBot(new ApplyPowerAction(p,p,new SinkIntoDarkPower2(p,this.secondaryM,this.magicNumber)));
        }else {
            addToBot(new ApplyPowerAction(p,p,new SinkIntoDarkPower(p,this.secondaryM,this.magicNumber)));
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }
}
