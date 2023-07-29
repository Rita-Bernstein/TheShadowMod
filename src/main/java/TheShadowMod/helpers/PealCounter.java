package TheShadowMod.helpers;

import TheShadowMod.powers.TheShadow.EchoPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PealCounter {
    public int pealAppliedCount = 0;

    public PealCounter() {
    }

    public void atStartOfTurn() {
        pealAppliedCount = 0;
    }

}
