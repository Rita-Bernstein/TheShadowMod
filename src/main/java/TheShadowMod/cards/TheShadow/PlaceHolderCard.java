package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.helpers.BackCardManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlaceHolderCard extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(PlaceHolderCard.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Strike_TS.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public PlaceHolderCard() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        if (thisCopy != null && thisCopy != this) {
            this.thisCopy.use(p, m);
        }
    }

    @Override
    public boolean canUpgrade() {
        if (this.thisCopy != null) {
            return this.thisCopy.canUpgrade() || this.backCard.canUpgrade();
        }

        return super.canUpgrade();
    }

    @Override
    public void EndTurnPlayThisCard() {
        if (this.thisCopy != null)
            this.thisCopy.triggerOnEndOfTurnForPlayingCard();
    }
}
