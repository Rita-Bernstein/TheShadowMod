package TheShadowMod.helpers;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.patches.AbstractCardPatches;
import TheShadowMod.patches.IndexCardsPatches;
import basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue.Save;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class SaveHelper {
    public static boolean noPotion = false;
    public static int nextCombatDamage = 0;
    public static int increaseMaxHP = 0;
    // 进入这一层时，清除信息并保存
    public static int clearIncreaseMaxHPFloorNum = -1;
    public static boolean rewardNewAct = true;

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

    public static void saveIncreaseMaxHP() {

        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.setInt(CardCrawlGame.saveSlot + "increaseMaxHP", increaseMaxHP);


            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();

            clearIncreaseMaxHPFloorNum = -1;
            nextCombatDamage = config.getInt(CardCrawlGame.saveSlot + "nextCombatDamage");
            increaseMaxHP = config.getInt(CardCrawlGame.saveSlot + "increaseMaxHP");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadNoPotion() {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();
            noPotion = config.getBool(CardCrawlGame.saveSlot + "noPotion");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveDeckBackCard() {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);

            if (AbstractDungeon.player != null && !AbstractDungeon.player.masterDeck.group.isEmpty()) {
                for (int i = 0; i < AbstractDungeon.player.masterDeck.group.size(); i++) {
                    AbstractCard card = AbstractDungeon.player.masterDeck.group.get(i);

                    if (card instanceof AbstractTSCard) {
                        AbstractTSCard c = (AbstractTSCard) card;

                        if (c.thisCopy == null || card.cardID.equals(c.thisCopy.cardID)) {
                            config.setString(CardCrawlGame.saveSlot + "frontCardID" + i, Madness.ID);
                            config.setInt(CardCrawlGame.saveSlot + "frontCardUpgrades" + i, -1);
                            config.setInt(CardCrawlGame.saveSlot + "frontCardMisc" + i, 0);
                        } else {
                            config.setString(CardCrawlGame.saveSlot + "frontCardID" + i, c.thisCopy.cardID);
                            config.setInt(CardCrawlGame.saveSlot + "frontCardUpgrades" + i, c.thisCopy.timesUpgraded);
                            config.setInt(CardCrawlGame.saveSlot + "frontCardMisc" + i, c.thisCopy.misc);
                        }


                        if (c.backCard == null || card.cardID.equals(c.backCard.cardID)) {
                            config.setString(CardCrawlGame.saveSlot + "backCardID" + i, Madness.ID);
                            config.setInt(CardCrawlGame.saveSlot + "backCardUpgrades" + i, -1);
                            config.setInt(CardCrawlGame.saveSlot + "backCardMisc" + i, 0);
                        } else {
                            config.setString(CardCrawlGame.saveSlot + "backCardID" + i, c.backCard.cardID);
                            config.setInt(CardCrawlGame.saveSlot + "backCardUpgrades" + i, c.backCard.timesUpgraded);
                            config.setInt(CardCrawlGame.saveSlot + "backCardMisc" + i, c.backCard.misc);
                        }

                    }
                }
            }

            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDeckBackCard(AbstractPlayer p) {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();

            if (AbstractDungeon.player != null && !AbstractDungeon.player.masterDeck.group.isEmpty()) {
                for (int i = 0; i < AbstractDungeon.player.masterDeck.group.size(); i++) {
                    AbstractCard card = AbstractDungeon.player.masterDeck.group.get(i);

                    if (card instanceof AbstractTSCard) {
                        AbstractTSCard c = (AbstractTSCard) card;

                        int frontUpgrades = config.getInt(CardCrawlGame.saveSlot + "frontCardUpgrades" + i);
                        int frontMisc = config.getInt(CardCrawlGame.saveSlot + "frontCardMisc" + i);
                        String frontCardID = config.getString(CardCrawlGame.saveSlot + "frontCardID" + i);

                        int backCardUpgrades = config.getInt(CardCrawlGame.saveSlot + "backCardUpgrades" + i);
                        int backCardMisc = config.getInt(CardCrawlGame.saveSlot + "backCardMisc" + i);
                        String backCardID = config.getString(CardCrawlGame.saveSlot + "backCardID" + i);


                        System.out.println("===============" + card.name);
                        System.out.println("backCardID ====" + backCardID);
                        System.out.println("backCardMisc ====" + backCardMisc);
                        System.out.println("backCardUpgrades ====" + backCardUpgrades);
                        System.out.println("card.cardID.equals(backCardID) ====" + (card.cardID.equals(backCardID)));

                        if (frontUpgrades >= 0) {
                            if (frontCardID != null && !card.cardID.equals(frontCardID)) {
                                BackCardManager.setCardToFrontCard(CardLibrary.getCopy(frontCardID, frontUpgrades, frontMisc), card, true);
                            }else {
                                c.thisCopy = c;
                            }
                        } else {
                            c.thisCopy = c;
                        }

                        if (backCardUpgrades >= 0) {
                            if (backCardID != null && !card.cardID.equals(backCardID)) {
                                c.setBackCardFromID(card, backCardID, backCardUpgrades, backCardMisc);
                            }else {
                                c.initializeBackCard();
                            }
                        } else {
                            c.initializeBackCard();
                        }
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveCardOrderIndex() {

        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            if (AbstractDungeon.player != null) {
                int count = 0;
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    config.setInt(CardCrawlGame.saveSlot + "CardIndex" + count, IndexCardsPatches.getCardIndex(card));
                    count++;
                }
            }
            config.setInt(CardCrawlGame.saveSlot + "CardIndex", nextCombatDamage);


            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCardOrderIndex(AbstractPlayer p) {
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


    public static void SaveRewardCard(ArrayList<AbstractCard> cards) {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.setBool(CardCrawlGame.saveSlot + "rewardNewAct", SaveHelper.rewardNewAct);

            for (int i = 0; i < cards.size(); i++)
                config.setString(CardCrawlGame.saveSlot + "RewardCard" + i, cards.get(i).cardID);


            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i) instanceof AbstractTSCard) {
                    AbstractTSCard c = (AbstractTSCard) cards.get(i);
                    if (c.backCard != null) {
                        config.setInt(CardCrawlGame.saveSlot + "RewardBackCardUpgrades" + i, c.backCard.timesUpgraded);
                        config.setInt(CardCrawlGame.saveSlot + "RewardBackCardMisc" + i, c.backCard.misc);
                        config.setString(CardCrawlGame.saveSlot + "RewardBackCardID" + i, c.backCard.cardID);
                    }
                }
            }


            config.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SaveRewardCard() {
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.setBool(CardCrawlGame.saveSlot + "rewardNewAct", SaveHelper.rewardNewAct);

            config.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<AbstractCard> loadRewardCard() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        try {
            SaveConfig config = new SaveConfig(TheShadowMod.TheShadowModDefaults);
            config.load();

            SaveHelper.rewardNewAct = config.getBool(CardCrawlGame.saveSlot + "rewardNewAct");

            for (int i = 0; i < 3; i++)
                cards.add(CardLibrary.getCopy(config.getString(CardCrawlGame.saveSlot + "RewardCard" + i)));

            for (int i = 0; i < 3; i++) {
                if (cards.get(i) instanceof AbstractTSCard) {
                    AbstractTSCard c = (AbstractTSCard) cards.get(i);
                    if (c.backCard != null) {
                        int backCardUpgrades = config.getInt(CardCrawlGame.saveSlot + "RewardBackCardUpgrades" + i);
                        int backCardMisc = config.getInt(CardCrawlGame.saveSlot + "RewardBackCardMisc" + i);
                        String backCardID = config.getString(CardCrawlGame.saveSlot + "RewardBackCardID" + i);


                        if (backCardUpgrades < 0) {
                            c.backCard = c.makeSameInstanceOf();
                            continue;
                        }


                        c.setBackCardFromID(cards.get(i), backCardID, backCardUpgrades, backCardMisc);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }


}
