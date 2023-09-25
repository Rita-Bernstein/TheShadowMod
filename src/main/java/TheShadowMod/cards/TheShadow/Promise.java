package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.actions.TheShadow.ApplyPealPowerAction;
import TheShadowMod.helpers.SaveHelper;
import TheShadowMod.patches.GameStatsPatch;
import TheShadowMod.powers.AbstractShadowModPower;
import TheShadowMod.powers.TheShadow.PealPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Promise extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(Promise.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/Promise.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Promise() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 10;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPealPowerAction(m, this.magicNumber));
        SaveHelper.noPotion = true;
        SaveHelper.saveNoPotion();

    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "addPotionToRewards",
            paramtypez = {}
    )
    public static class NoPotionPatch {
        @SpireInsertPatch(rloc = 789-765, localvars = {"chance"})
        public static SpireReturn<Void> Insert(AbstractRoom __instance, @ByRef int[] chance) {
            SaveHelper.loadNoPotion();
            if (SaveHelper.noPotion) {
                chance[0] = 0;
            }
            System.out.println("QAQQQQQ POTION CHANCE: " + chance[0]);
            return SpireReturn.Continue();
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(3);
        }
    }
}
