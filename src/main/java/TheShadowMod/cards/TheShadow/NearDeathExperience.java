package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NearDeathExperience extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(NearDeathExperience.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/NearDeathExperience.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public NearDeathExperience() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 5;
        this.secondaryM = this.baseSecondaryM = 5;
        this.exhaust = true;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.currentHealth = 0;
                p.healthBarUpdatedEvent();
                isDone = true;
            }
        });
        addToBot(new ExhaustAction(BaseMod.MAX_HAND_SIZE, true, false, false, Settings.ACTION_DUR_XFAST));
        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new GainFlipPowerAction(this.secondaryM));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
