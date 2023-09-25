package TheShadowMod.cards.TheShadow;

import TheShadowMod.cards.AbstractShadowModCard;
import TheShadowMod.patches.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractTSCard extends AbstractShadowModCard {
    public AbstractTSCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, img, cost, type, rarity, target);
        this.color = CardColorEnum.TheShadow_LIME;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    public void onFlip(AbstractTSCard thisCard, boolean flipThisSide) {

    }

    public void onFlipInHand(AbstractTSCard thisCard, boolean flipThisSide) {
    }



    public void useCommon(AbstractPlayer p, AbstractMonster m) {
    }


    public void cloneFieldCommon(AbstractCard ori) {
    }
}
