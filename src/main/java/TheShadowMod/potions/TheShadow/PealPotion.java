package TheShadowMod.potions.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.potions.AbstractShadowModPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class PealPotion extends AbstractShadowModPotion {
    public static final String POTION_ID = TheShadowMod.makeID(PealPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    private static final AbstractPotion.PotionRarity RARITY = PotionRarity.COMMON;
    private static final AbstractPotion.PotionSize SIZE = AbstractPotion.PotionSize.M;
    public static final Color liquidColor = new Color(0.02f, 0.02f, 0.02f, 1.0f);

    public PealPotion() {
        super(NAME, POTION_ID, RARITY, SIZE);
        this.isThrown = false;
        this.targetRequired = true;
        this.labOutlineColor = TheShadowMod.TheShadow_Color;
    }


    public void initializeData() {
        this.potency = getPotency();
        this.description = String.format(DESCRIPTIONS[0], this.potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(AbstractCreature target) {
        addToBot(new ApplyPealPowerAction(target,this.potency));
    }


    public int getPotency(int ascensionLevel) {
        return 20;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new PealPotion();
    }
}
