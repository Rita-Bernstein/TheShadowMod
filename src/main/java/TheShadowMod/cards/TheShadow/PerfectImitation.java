package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

import java.util.ArrayList;

public class PerfectImitation extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(PerfectImitation.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/PerfectImitation.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public PerfectImitation() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractPotion> potions = new ArrayList<>();
        for (AbstractPotion potion : p.potions) {
            if (!(potion instanceof PotionSlot)) {
                potions.add(potion);
            }
        }


        if (potions.size() > 1) {
            potions.get(AbstractDungeon.cardRandomRng.random(potions.size() - 1)).use(m);
        } else if (potions.size() == 1) {
            potions.get(0).use(m);
        }

    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
