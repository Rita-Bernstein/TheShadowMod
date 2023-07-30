package TheShadowMod.helpers;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;

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


}
