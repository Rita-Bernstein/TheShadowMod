package TheShadowMod.potions;

import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class AbstractShadowModPotion  extends AbstractPotion {
    public AbstractShadowModPotion(String name, String id, PotionRarity rarity, PotionSize size) {
        super(name, id, rarity, size, AbstractPotion.PotionColor.STRENGTH);
    }


}
