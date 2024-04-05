package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.SwitchWorldAction;
import TheShadowMod.patches.CardTagsEnum;
import TheShadowMod.powers.TheShadow.FlipPower;
import TheShadowMod.powers.TheShadow.JumpOutPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JumpOut extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(JumpOut.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/JumpOut.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public JumpOut() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        tags.add(CardTagsEnum.Essence);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void useCommon(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(FlipPower.POWER_ID)) {
            addToBot(new SwitchWorldAction());
        } else {
            addToBot(new RemoveSpecificPowerAction(p, p, FlipPower.POWER_ID));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
