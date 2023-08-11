package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Airflow extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Airflow.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Airflow.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Airflow() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = 8;
        this.magicNumber = this.baseMagicNumber = 4;
        this.exhaust = true;
        this.exhaustOriginal = true;
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
    }


    @Override
    public void onFlipInHand(boolean isBack) {
        addToTop(new GainBlockAction(AbstractDungeon.player,AbstractDungeon.player,this.magicNumber));
        super.onFlipInHand(isBack);
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(4);
            upgradeMagicNumber(2);
        }
    }


}
