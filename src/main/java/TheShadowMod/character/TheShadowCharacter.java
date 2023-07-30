package TheShadowMod.character;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.*;
import TheShadowMod.modules.EnergyOrbCustomBlue;
import TheShadowMod.patches.AbstractPlayerEnum;
import TheShadowMod.patches.CardColorEnum;
import TheShadowMod.relics.TheShadow.MistyMirror;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;
import java.util.List;


public class TheShadowCharacter extends CustomPlayer {
    public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(TheShadowMod.makeID(TheShadowCharacter.class.getSimpleName()));

    public static final int ENERGY_PER_TURN = 3;
    public static final int START_HP = 52;
    public static final int START_GOLD = 99;


    public static final String[] orbTextures = {
            "TheShadowMod/img/ui/topPanel/TheShadow/00.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/01.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/02.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/03.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/04.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/00.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/01.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/02.png",
            "TheShadowMod/img/ui/topPanel/TheShadow/03.png",
    };

    public TheShadowCharacter(String name, PlayerClass setClass) {
        super(name, setClass, new EnergyOrbCustomBlue(orbTextures, "TheShadowMod/img/ui/topPanel/TheShadow/energyBlueVFX.png"), null, null);
        this.drawX += 5.0F * Settings.scale;
        this.drawY += 7.0F * Settings.scale;

        this.dialogX = this.drawX + 20.0F * Settings.scale;
        this.dialogY = this.drawY + 270.0F * Settings.scale;

        initializeClass("TheShadowMod/img/characters/TheShadow/idle.png",
                "TheShadowMod/img/characters/TheShadow/shoulder.png",
                "TheShadowMod/img/characters/TheShadow/shoulder2.png",
                "TheShadowMod/img/characters/TheShadow/corpse.png",
                getLoadout(), 0.0F, -5.0F, 240.0F, 280.0F, new EnergyManager(ENERGY_PER_TURN));


    }


    public String getPortraitImageName() {
        return null;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MistyMirror.ID);

        return retVal;
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_TS.ID);
        retVal.add(Strike_TS.ID);
        retVal.add(Strike_TS.ID);
        retVal.add(Strike_TS.ID);
        retVal.add(Defend_TS.ID);
        retVal.add(Defend_TS.ID);
        retVal.add(Defend_TS.ID);
        retVal.add(Defend_TS.ID);
        retVal.add(CrossExamine.ID);
        retVal.add(Merge.ID);

        return retVal;
    }


    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                getLocalizedCharacterName(),
                charStrings.TEXT[0],
                START_HP,
                START_HP,
                0,
                START_GOLD,
                5,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false);
    }


    @Override
    public String getTitle(PlayerClass playerClass) {
        return charStrings.NAMES[1];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return CardColorEnum.TheShadow_LIME;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new CrossExamine();
    }

    @Override
    public Color getCardTrailColor() {
        return TheShadowMod.TheShadow_Color.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 2;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return TheShadowMod.makeID("FP_Selected");
    }

    @Override
    public String getLocalizedCharacterName() {
        return charStrings.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheShadowCharacter(this.name, AbstractPlayerEnum.TheShadow);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.WHITE;
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    @Override
    public Color getCardRenderColor() {
        return Color.WHITE;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel("TheShadowMod/img/characters/TheShadow/victory/01.png"));
        panels.add(new CutscenePanel("TheShadowMod/img/characters/TheShadow/victory/02.png"));
        panels.add(new CutscenePanel("TheShadowMod/img/characters/TheShadow/victory/03.png"));
        return panels;
    }


}

