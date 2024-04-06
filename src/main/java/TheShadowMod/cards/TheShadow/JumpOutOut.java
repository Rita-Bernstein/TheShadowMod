package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.SwitchWorldAction;
import TheShadowMod.cards.AbstractShadowModCard;
import TheShadowMod.patches.GameStatsPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JumpOutOut extends AbstractShadowModCard {
    public static final String ID = TheShadowMod.makeID(JumpOutOut.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/JumpOut.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public JumpOutOut() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.color = CardColor.COLORLESS;
        this.exhaust = true;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void onChoseThisOption() {
        if(GameStatsPatch.blackWorld){
            addToBot(new SwitchWorldAction());
        }
    }

    @Override
    public void upgrade() {
    }
}
