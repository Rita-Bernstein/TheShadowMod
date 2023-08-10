package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.BloodyPathPower;
import TheShadowMod.powers.TheShadow.HeavyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;

public class BloodyPath extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(BloodyPath.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/BloodyPath.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BloodyPath() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.secondaryM = this.baseSecondaryM = 1;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new ApplyPowerAction(p,p,new BloodyPathPower(p,this.secondaryM)));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
