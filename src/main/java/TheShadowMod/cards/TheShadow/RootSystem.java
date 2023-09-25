package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.HeavyPower;
import TheShadowMod.powers.TheShadow.RootSystemPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RootSystem extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(RootSystem.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/RootSystem.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public RootSystem() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;

    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RootSystemPower(p, this.magicNumber)));
        if (this.upgraded) {
            addToBot(new ApplyPowerAction(p,p,new HeavyPower(p, 1)));
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
