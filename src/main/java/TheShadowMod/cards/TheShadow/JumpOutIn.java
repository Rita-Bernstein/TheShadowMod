package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.ApplyPowerToAllEnemyAction;
import TheShadowMod.actions.TheShadow.SwitchWorldAction;
import TheShadowMod.cards.AbstractShadowModCard;
import TheShadowMod.patches.GameStatsPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.screens.GameOverScreen;

public class JumpOutIn extends AbstractShadowModCard {
    public static final String ID = TheShadowMod.makeID(JumpOutIn.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/JumpOut.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public JumpOutIn() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.color = CardColor.COLORLESS;
        this.exhaust = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void onChoseThisOption() {
        if(!GameStatsPatch.blackWorld){
            addToBot(new SwitchWorldAction());
        }
    }

    @Override
    public void upgrade() {
    }
}
