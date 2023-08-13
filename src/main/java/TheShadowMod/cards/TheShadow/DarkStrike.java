package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.helpers.SaveHelper;
import TheShadowMod.powers.TheShadow.LoseMaxHpPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class DarkStrike extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DarkStrike.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DarkStrike.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public DarkStrike() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 2;
        this.tags.add(CardTags.STRIKE);

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p,p,this.magicNumber));
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p.currentHealth > 0)
                    p.decreaseMaxHealth(magicNumber);
                SaveHelper.increaseMaxHP += magicNumber;
                isDone = true;
            }
        });
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {SaveFile.class}
    )
    public static class OnEnterRoomPatch {
        @SpireInsertPatch(rloc = 89)
        public static SpireReturn<Void> Insert(AbstractDungeon _instance, SaveFile saveFile) {
            System.out.println("DARKKK STRIIIIIKE1: " + SaveHelper.increaseMaxHP);

            if (SaveHelper.clearIncreaseMaxHPFloorNum == AbstractDungeon.floorNum) {
                SaveHelper.clearIncreaseMaxHPFloorNum = -1;
                SaveHelper.saveIncreaseMaxHP();
                System.out.println("DARKKK STRIIIIIKE2: " + SaveHelper.increaseMaxHP);
                return SpireReturn.Continue();
            } else {
                SaveHelper.loadSettings();
            }

            if (AbstractDungeon.nextRoom.room instanceof com.megacrit.cardcrawl.rooms.RestRoom) {
                AbstractDungeon.player.increaseMaxHp(SaveHelper.increaseMaxHP, true);
                SaveHelper.increaseMaxHP = 0;
                SaveHelper.clearIncreaseMaxHPFloorNum = AbstractDungeon.floorNum + 1;

            } else {
                SaveHelper.saveIncreaseMaxHP();
            }

            System.out.println("DARKKK STRIIIIIKE3: " + SaveHelper.increaseMaxHP);
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class OnVictoryPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            SaveHelper.saveIncreaseMaxHP();
            return SpireReturn.Continue();
        }
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }
}
