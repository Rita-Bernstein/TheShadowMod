package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.Common.BetterIncreaseMiscAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Bone extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Bone.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Bone.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Bone() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.misc = 15;
        this.baseBlock = this.misc;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    public void applyPowers() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                this.baseBlock = c.misc;
                super.applyPowers();
                initializeDescription();
                return;
            }
        }
    }

    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new BetterIncreaseMiscAction(this.uuid, -this.magicNumber, card -> {
            baseBlock = card.misc;
            card.applyPowers();
            card.initializeDescription();
        }));
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(this.uuid)) {
                    c.misc += 5;
                }
            }

            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
                c.misc += 5;
            }
        }
    }
}
