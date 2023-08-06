package TheShadowMod.relics.TheShadow;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface NotDeadRelic {

    boolean onDead(AbstractPlayer var1, DamageInfo var2);
}
