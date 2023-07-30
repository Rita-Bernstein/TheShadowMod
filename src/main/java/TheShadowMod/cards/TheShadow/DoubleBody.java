package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.PlayTmpCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DoubleBody extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DoubleBody.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DoubleBody.png");
    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DoubleBody() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.isEthereal = true;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onThisFlipInHand() {
        ArrayList<AbstractCard> list = new ArrayList<>(AbstractDungeon.player.hand.group);
        list.removeIf(card -> card == this);
        if(list.size()>1){
            addToTop(new PlayTmpCardAction(list.get(AbstractDungeon.cardRandomRng.random(list.size()-1))));
        }else if(list.size()==1){
            addToTop(new PlayTmpCardAction(list.get(0)));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isEthereal = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
