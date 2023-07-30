package TheShadowMod.actions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.LoseMaxHpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

public class TempIncreaseMaxHPAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheShadowMod.makeID("TempIncreaseMaxHPAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private int amount;
    private AbstractPlayer p;

    public TempIncreaseMaxHPAction(AbstractPlayer p,int amount) {
        this.amount = amount;
        this.p = p;
        this.duration = Settings.ACTION_DUR_FAST;
    }


    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (!Settings.isEndless || !AbstractDungeon.player.hasBlight("FullBelly")) {

                p.increaseMaxHp(this.amount,true);
                addToBot(new ApplyPowerAction(p,p,new LoseMaxHpPower(p,this.amount),this.amount));
            }
        }
        tickDuration();
    }
}


