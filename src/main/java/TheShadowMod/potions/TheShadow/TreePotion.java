package TheShadowMod.potions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.potions.AbstractShadowModPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class TreePotion extends AbstractShadowModPotion {
    public static final String POTION_ID = TheShadowMod.makeID(TreePotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    private static final PotionRarity RARITY = PotionRarity.RARE;
    private static final PotionSize SIZE = PotionSize.BOTTLE;
    public static final Color liquidColor = new Color(0.02f, 0.02f, 0.02f, 1.0f);

    public TreePotion() {
        super(NAME, POTION_ID, RARITY, SIZE);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = TheShadowMod.TheShadow_Color;
    }


    public void initializeData() {
        this.potency = getPotency();
        this.description = DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(AbstractCreature target) {
        addToBot(new HealAction(AbstractDungeon.player,AbstractDungeon.player,AbstractDungeon.player.maxHealth));
    }


    public int getPotency(int ascensionLevel) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TreePotion();
    }
}
