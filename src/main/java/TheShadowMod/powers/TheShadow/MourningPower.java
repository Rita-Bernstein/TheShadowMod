package TheShadowMod.powers.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class MourningPower extends AbstractShadowModPower {
    public static final String POWER_ID = TheShadowMod.makeID(MourningPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public MourningPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        updateDescription();
        // 先涨潮，再既视感，最后哀恸
        this.priority = 100;

        loadShadowRegion("MourningPower");
    }


    @Override
    public void atStartOfTurnPostDraw() {
        // 先既视感再哀恸也可以触发
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if(GameStatsPatch.blackWorld){
                    flash();
                    addToTop(new ArmamentsAction(true));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description =  DESCRIPTIONS[0];
    }
}