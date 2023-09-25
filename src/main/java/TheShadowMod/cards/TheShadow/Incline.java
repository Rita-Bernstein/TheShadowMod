package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.SelectHandCardAction;
import TheShadowMod.actions.TheShadow.GainFlipPowerAction;
import TheShadowMod.actions.TheShadow.InclineAction;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Incline extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Incline.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Incline.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Incline() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void useCommon(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainFlipPowerAction(this.magicNumber));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void update() {
        super.update();

        if (AbstractDungeon.currMapNode == null || (AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }


        int finalCost = 1;


        AbstractCard bCard = BackCardManager.AddFields.backCard.get(this);


        if (AbstractDungeon.player != null)
            if (this.upgraded || (bCard != null && bCard.cardID.equals(cardID) && bCard.upgraded))
                finalCost = (GameActionManager.turn + 2) % 3;
            else {
                finalCost = GameActionManager.turn % 3;
            }

        if (bCard != null) {
            bCard.setCostForTurn(finalCost);
        }

        setCostForTurn(finalCost);
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
