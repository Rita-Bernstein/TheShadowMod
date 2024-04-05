package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.FlipCardAction;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import TheShadowMod.patches.CardTagsEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Confusion extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Confusion.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Confusion.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Confusion() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.tags.add(CardTagsEnum.Essence);
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainFlipPowerAction(this.magicNumber));
    }

    @Override
    public void tookDamage() {
        super.tookDamage();
        addToTop(new FlipCardAction(this));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
