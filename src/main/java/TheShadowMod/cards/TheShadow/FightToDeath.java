package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.DoublePowerAction;
import TheShadowMod.powers.TheShadow.HeavyPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FightToDeath extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(FightToDeath.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/FightToDeath.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public FightToDeath() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;

    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DoublePowerAction(new HeavyPower(p,1)));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
