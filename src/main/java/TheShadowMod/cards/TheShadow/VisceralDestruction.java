package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VisceralDestruction extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(VisceralDestruction.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/VisceralDestruction.png");
    private static final int COST = 3;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public VisceralDestruction() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 32;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPealPowerAction(m,this.magicNumber));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(10);
        }
    }
}
