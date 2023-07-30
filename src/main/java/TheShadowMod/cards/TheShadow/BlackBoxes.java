package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.BlackBoxesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlackBoxes extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(BlackBoxes.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/BlackBoxes.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BlackBoxes() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BlackBoxesAction());
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
