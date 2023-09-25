package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HiddenDoor extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(HiddenDoor.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/HiddenDoor.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public HiddenDoor() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = 7;
        this.selfRetain = true;
        this.cardsToPreview = new Wallcovering();
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new MakeTempCardInHandAction(new Wallcovering()));
    }

    @Override
    public void cloneFieldCommon(AbstractCard ori) {
        ori.selfRetain = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(3);
        }
    }
}
