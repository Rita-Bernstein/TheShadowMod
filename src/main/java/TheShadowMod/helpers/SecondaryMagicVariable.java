package TheShadowMod.helpers;


import TheShadowMod.cards.AbstractShadowModCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondaryMagicVariable extends DynamicVariable {
    @Override
    public String key() {
        return "TheShadowSM";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractShadowModCard) {
            AbstractShadowModCard asc = (AbstractShadowModCard) card;
            return asc.isSecondaryMModified;
        } else {
            return false;
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractShadowModCard) {
            AbstractShadowModCard asc = (AbstractShadowModCard) card;
            return asc.secondaryM;
        } else {
            return 0;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractShadowModCard) {
            AbstractShadowModCard asc = (AbstractShadowModCard) card;
            return asc.baseSecondaryM;
        } else {
            return 0;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractShadowModCard) {
            AbstractShadowModCard asc = (AbstractShadowModCard) card;
            return asc.upgradeSecondaryM;
        } else {
            return false;
        }
    }
}