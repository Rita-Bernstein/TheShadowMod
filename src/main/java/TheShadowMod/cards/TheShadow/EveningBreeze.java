package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.helpers.SaveHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

public class EveningBreeze extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(EveningBreeze.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/EveningBreeze.png");
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public EveningBreeze() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        SaveHelper.nextCombatDamage += this.magicNumber;
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatLogic"
    )
    public static class PreBattlePrepPatch {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn<Void> Insert(AbstractPlayer _instance) {
            SaveHelper.loadSettings();

            if(SaveHelper.nextCombatDamage >0) {
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(
                                AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX, AbstractDungeon.player.hb.cY + AbstractDungeon.player.hb.height / 2.0F - AbstractDungeon.player.animY,
                                CardCrawlGame.languagePack.getCardStrings(EveningBreeze.ID).NAME, Settings.GREEN_TEXT_COLOR));
                        isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null,
                        DamageInfo.createDamageMatrix(SaveHelper.nextCombatDamage, true), DamageInfo.DamageType.THORNS,

                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
            SaveHelper.nextCombatDamage = 0;

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
            SaveHelper.saveNextCombatDamage();
            return SpireReturn.Continue();
        }
    }


    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(4);
        }
    }
}
