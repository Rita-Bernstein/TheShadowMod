package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.actions.TheShadow.InclineAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Incline extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Incline.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Incline.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Incline() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new InclineAction());
    }

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null)
            if (this.upgraded)
                this.cost = this.costForTurn = (GameActionManager.turn + 2) % 3;
            else
                this.cost = this.costForTurn = GameActionManager.turn % 3;
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
