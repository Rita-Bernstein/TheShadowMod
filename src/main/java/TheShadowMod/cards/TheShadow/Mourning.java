package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import TheShadowMod.powers.TheShadow.HeavyPower;
import TheShadowMod.powers.TheShadow.MourningPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Mourning extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Mourning.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Mourning.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Mourning() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            addToBot(new GainFlipPowerAction(this.magicNumber));
        }
        addToBot(new ApplyPowerAction(p,p,new MourningPower(p)));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
