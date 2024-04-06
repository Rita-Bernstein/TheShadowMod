package TheShadowMod;

import TheShadowMod.cards.TheShadow.*;
import TheShadowMod.character.TheShadowCharacter;
import TheShadowMod.helpers.SaveHelper;
import TheShadowMod.helpers.SecondaryMagicVariable;
import TheShadowMod.helpers.ViewFlipButton;
import TheShadowMod.patches.AbstractPlayerEnum;
import TheShadowMod.patches.CardColorEnum;
import TheShadowMod.patches.FlipCardEffectPatches;
import TheShadowMod.potions.TheShadow.*;
import TheShadowMod.relics.TheShadow.*;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpireInitializer
public class TheShadowMod implements
        PostInitializeSubscriber,
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        AddAudioSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber,
        PostDungeonInitializeSubscriber,
        StartActSubscriber,
        StartGameSubscriber {

    public static final String MODNAME = "TheShadowMod";
    public static final String AUTHOR = "Rita";
    public static final String DESCRIPTION = "";
    public static final Color TheShadow_Color = new Color(0.376F, 0.376F, 0.376F, 1.0F);
    public static final Logger logger = LogManager.getLogger(TheShadowMod.class.getSimpleName());

    public static String MOD_ID = "TheShadowMod";
    public static Properties TheShadowModDefaults = new Properties();

    public static ArrayList<AbstractCard> shadowCardPool = new ArrayList<>();
    public static ArrayList<AbstractCard> shadowCardCommonPool = new ArrayList<>();
    public static ArrayList<AbstractCard> shadowCardUncommonPool = new ArrayList<>();
    public static ArrayList<AbstractCard> shadowCardRarePool = new ArrayList<>();

    public TheShadowMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(CardColorEnum.TheShadow_LIME,
                TheShadow_Color, TheShadow_Color, TheShadow_Color, TheShadow_Color, TheShadow_Color, TheShadow_Color, TheShadow_Color,
                assetPath("img/cardui/TheShadow/512/bg_attack_lime.png"),
                assetPath("img/cardui/TheShadow/512/bg_skill_lime.png"),
                assetPath("img/cardui/TheShadow/512/bg_power_lime.png"),
                assetPath("img/cardui/TheShadow/512/card_lime_orb.png"),
                assetPath("img/cardui/TheShadow/1024/bg_attack_lime.png"),
                assetPath("img/cardui/TheShadow/1024/bg_skill_lime.png"),
                assetPath("img/cardui/TheShadow/1024/bg_power_lime.png"),
                assetPath("img/cardui/TheShadow/1024/card_lime_orb.png"),
                assetPath("img/cardui/TheShadow/512/card_lime_small_orb.png")
        );


    }


    public static String makeID(String id) {
        return MOD_ID + ":" + id;
    }

    public static String assetPath(String path) {
        return MOD_ID + "/" + path;
    }

    public static String characterAssetPath(String className, String path) {
        return MOD_ID + "/" + className + "/" + path;
    }


    public static void initialize() {
        new TheShadowMod();
    }

    @Override
    public void receivePostInitialize() {
        SaveHelper.loadSettings();
//        Texture badgeTexture = new Texture(assetPath("/img/badge.png"));
//        ModPanel settingsPanel = new ModPanel();
//        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);


        BaseMod.addPotion(PealPotion.class, PealPotion.liquidColor, PealPotion.liquidColor, null, PealPotion.POTION_ID);
        BaseMod.addPotion(FlipPotion.class, FlipPotion.liquidColor, FlipPotion.liquidColor, null, FlipPotion.POTION_ID);
        BaseMod.addPotion(TreePotion.class, TreePotion.liquidColor, TreePotion.liquidColor, null, TreePotion.POTION_ID);

//        BaseMod.addPotion(PowerPotion.class, PowerPotion.liquidColor, PowerPotion.liquidColor, null, PowerPotion.POTION_ID, AbstractPlayerEnum.TimeCode);
    }

    @Override
    public void receiveAddAudio() {
//        BaseMod.addAudio(makeID("FP_Selected"), assetPath("/audio/sound/FP_Selected.wav"));
//        BaseMod.addAudio(makeID("TC_Selected"), assetPath("/audio/sound/TC_Selected.wav"));
    }

    @Override
    public void receivePostDungeonInitialize() {
        System.out.println("重开游戏");

    }

    @Override
    public void receiveStartAct() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);

        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            numCards = r.changeNumberOfCardsInReward(numCards);
        }

        if (ModHelper.isModEnabled("Binary")) {
            numCards--;
        }

        for (int i = 0; i < numCards; i++) {
            while (retVal.contains(card)) {
                card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);
            }
            retVal.add(card);
        }

        ArrayList<AbstractCard> retVal2 = new ArrayList<AbstractCard>();
        for (AbstractCard c : retVal) {
            retVal2.add(c.makeCopy());
        }

        SaveHelper.SaveRewardCard(retVal2);

        //================提前产生关底boss里表

        if (AbstractDungeon.floorNum == 0) {
            SaveHelper.increaseMaxHP = 0;
            SaveHelper.saveIncreaseMaxHP();
            SaveHelper.nextCombatDamage = 0;
            SaveHelper.saveNextCombatDamage();
        }
    }

    @Override
    public void receiveStartGame() {
        ViewFlipButton.isViewingFlip = false;
        FlipCardEffectPatches.flipBgOffset = -1.0f;
    }


    @Override
    public void receiveEditCharacters() {
        logger.info("========================= 开始加载人物 =========================");

        BaseMod.addCharacter(new TheShadowCharacter(TheShadowCharacter.charStrings.NAMES[1], AbstractPlayerEnum.TheShadow),
                assetPath("img/characters/TheShadow/Button.png"),
                assetPath("img/characters/TheShadow/portrait.jpg"),
                AbstractPlayerEnum.TheShadow);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new SecondaryMagicVariable());
        List<CustomCard> cards = new ArrayList<>();

//-------------------------------------------冰霜牧师--------------------------------------------
//
//
//
//          基础牌
        cards.add(new Strike_TS());
        cards.add(new Defend_TS());
        cards.add(new CrossExamine());
        cards.add(new Merge());

//        普通牌
        cards.add(new BackStrike());
        cards.add(new Intrusion());
        cards.add(new Shroud());
        cards.add(new EnucleationHeart());
        cards.add(new Promise());
        cards.add(new Annihilate());
        cards.add(new ShadowTouch());
        cards.add(new ScaldingColloid());
        cards.add(new EternalPunishment());
        cards.add(new Idealistic());
        cards.add(new HiddenDoor());
        cards.add(new Tinnitus());
        cards.add(new DeepDiving());
        cards.add(new Incline());
        cards.add(new DeadAngle());
        cards.add(new Ultrasonic());
        cards.add(new EmptyShell());
        cards.add(new Confusion());
        cards.add(new Airflow());
        cards.add(new SpiralStaircase());
        cards.add(new FightToDeath());
        cards.add(new VoidPace());

//        罕见
        cards.add(new ShadowStrike());
        cards.add(new TombstonePiledriver());
        cards.add(new Phoenix());
        cards.add(new DarkStrike());
        cards.add(new Assassin());
        cards.add(new WarWithWar());
        cards.add(new TestWater());
        cards.add(new Contact());
        cards.add(new DarkMatter());
        cards.add(new Instantiate());
        cards.add(new Hidden());
        cards.add(new PolarNight());
        cards.add(new Illusion());
        cards.add(new DominatingFuture());
        cards.add(new Bone());
        cards.add(new DesperateAttempt());
        cards.add(new Fluctuate());
        cards.add(new Twins());
        cards.add(new BlackBoxes());
        cards.add(new BloodyPath());
        cards.add(new FloodTide());
        cards.add(new SonicBoom());
        cards.add(new Separation());
        cards.add(new Reconfiguration());
        cards.add(new Echo());
        cards.add(new SinkIntoDark());
        cards.add(new JumpOut());
        cards.add(new DejaVu());
        cards.add(new Reborn());
        cards.add(new Timing());
        cards.add(new EveningBreeze());
        cards.add(new RootSystem());

//        稀有牌
        cards.add(new BothDestruction());
        cards.add(new Hatred());
        cards.add(new Crumb());
        cards.add(new UnmarkedCommonGraves());
        cards.add(new PerfectImitation());
        cards.add(new DoubleBody());
        cards.add(new VisceralDestruction());
        cards.add(new TrueSight());
        cards.add(new NearDeathExperience());
        cards.add(new UltimateReaction());
        cards.add(new RealityForm());
        cards.add(new LingeringDeath());
        cards.add(new Mourning());
        cards.add(new Contemplate());
        cards.add(new Eclipse());
        cards.add(new Peel());
        cards.add(new GatheringDarkness());

//        特殊派
        cards.add(new Wallcovering());
        cards.add(new IllusionVul());
        cards.add(new IllusionWeak());
        cards.add(new Execute());


        for (CustomCard card : cards) {
            BaseMod.addCard(card);
            UnlockTracker.unlockCard(card.cardID);

            if (card instanceof AbstractTSCard) {
                if (card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.SPECIAL)
                    shadowCardPool.add(card);
            }


        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new MistyMirror(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new BrokenMirror(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new BloodyGauze(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new Knell(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new PyramidHead(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new Waterphone(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new Empties(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new Othello(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new DarkCore(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new HeavyWeight(), CardColorEnum.TheShadow_LIME);
        BaseMod.addRelicToCustomPool(new VocalCords(), CardColorEnum.TheShadow_LIME);

//        UnlockTracker.markRelicAsSeen(MistyMirror.ID);
//        UnlockTracker.markRelicAsSeen(BrokenMirror.ID);
//        UnlockTracker.markRelicAsSeen(BloodyGauze.ID);
//        UnlockTracker.markRelicAsSeen(Knell.ID);
//        UnlockTracker.markRelicAsSeen(PyramidHead.ID);
//        UnlockTracker.markRelicAsSeen(Waterphone.ID);
//        UnlockTracker.markRelicAsSeen(Empties.ID);
//        UnlockTracker.markRelicAsSeen(Othello.ID);
//        UnlockTracker.markRelicAsSeen(DarkCore.ID);
//        UnlockTracker.markRelicAsSeen(HeavyWeight.ID);
//        UnlockTracker.markRelicAsSeen(VocalCords.ID);

    }


    private Settings.GameLanguage languageSupport() {
        switch (Settings.language) {
            case ZHS:
                return Settings.language;
            case ZHT:
                return Settings.language;
            default:
                return Settings.GameLanguage.ENG;
        }
    }

    public void receiveEditStrings() {
        Settings.GameLanguage language = languageSupport();
        loadLocStrings(Settings.GameLanguage.ENG);
        if (!language.equals(Settings.GameLanguage.ENG)) {
            loadLocStrings(language);
        }

    }

    private void loadLocStrings(Settings.GameLanguage language) {
        String path = "localization/" + language.toString().toLowerCase() + "/";

        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(path + "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath(path + "CharacterStrings.json"));
//        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath(path + "EventStrings.json"));
//      BaseMod.loadCustomStringsFile(MonsterStrings.class, assetPath(path + "MonsterStrings.json"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, assetPath(path + "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, assetPath(path + "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath(path + "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath(path + "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath(path + "UIStrings.json"));
//        BaseMod.loadCustomStringsFile(StanceStrings.class, assetPath(path + "StanceStrings.json"));
//        BaseMod.loadCustomStringsFile(BlightStrings.class, assetPath(path + "BlightStrings.json"));

//        BaseMod.loadCustomStringsFile(TutorialStrings.class,assetPath(path + "TutorialStrings.json"));
    }


    private void loadLocKeywords(Settings.GameLanguage language) {
        String path = "localization/" + language.toString().toLowerCase() + "/";
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath(path + "KeywordStrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("the_shadow_mod", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }

    }

    @Override
    public void receiveEditKeywords() {

        Settings.GameLanguage language = languageSupport();

        // Load english first to avoid crashing if translation doesn't exist for something
        loadLocKeywords(Settings.GameLanguage.ENG);
        if (!language.equals(Settings.GameLanguage.ENG)) {
            loadLocKeywords(language);
        }
    }

}
