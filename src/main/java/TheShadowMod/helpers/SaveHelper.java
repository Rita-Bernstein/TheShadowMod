package TheShadowMod.helpers;

import TheShadowMod.TheShadowMod;
import TheShadowMod.patches.AbstractCardPatches;
import TheShadowMod.patches.IndexCardsPatches;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SaveHelper {
    public static boolean noPotion = false;
    public static int nextCombatDamage = 0;

    public static void saveNoPotion() {

        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.setBool(CardCrawlGame.saveSlot + "noPotion", noPotion);


            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveNextCombatDamage() {

        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.setInt(CardCrawlGame.saveSlot + "nextCombatDamage", nextCombatDamage);


            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();
            noPotion = config.getBool(CardCrawlGame.saveSlot + "noPotion");
            nextCombatDamage = config.getInt(CardCrawlGame.saveSlot + "nextCombatDamage");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveCardIndex() {

        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            if (AbstractDungeon.player != null) {
                int count = 0;
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    config.setInt(CardCrawlGame.saveSlot + "CardIndex" + count,  IndexCardsPatches.getCardIndex(card));
                    count++;
                }
            }
            config.setInt(CardCrawlGame.saveSlot + "CardIndex", nextCombatDamage);


            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCardIndex(AbstractPlayer p) {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();

            int count = 0;
            for (AbstractCard card : p.masterDeck.group) {
                IndexCardsPatches.setCardIndex(card, config.getInt(CardCrawlGame.saveSlot + "CardIndex" + count));
                count++;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
