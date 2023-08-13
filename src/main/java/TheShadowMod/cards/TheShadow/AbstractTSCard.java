package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.AbstractShadowModCard;
import TheShadowMod.helpers.ViewFlipButton;
import TheShadowMod.patches.CardColorEnum;
import TheShadowMod.powers.TheShadow.AnnihilatePower;
import TheShadowMod.powers.TheShadow.RealityFormPower;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class AbstractTSCard extends AbstractShadowModCard implements CustomSavable<Integer> {
    public AbstractCard backCard;
    public AbstractCard thisCopy;
    public int backCardIndex = -1;
    public CardType cardTypeOriginal;
    public boolean exhaustOriginal;

    public boolean isViewingFlip = false;
    public boolean isFlip = false;
    public boolean flipOnUseOnce = false;
    public boolean doubleOnUseOnce = false;

    public AbstractTSCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, img, cost, type, rarity, target);
        cardTypeOriginal = type;
        this.color = CardColorEnum.TheShadow_LIME;
    }


    public void onFlip() {
        onViewingFlip();
        if (this.isFlip) {
            this.thisCopy = this.makeStatEquivalentCopy();
            ((AbstractTSCard) this.thisCopy).backCard = null;

            if (this.backCard != null) {
                cloneFieldToCard(this.backCard);
            }

        } else {
            if (this.thisCopy != null) {
                cloneFieldToCard(this.thisCopy);
            }
        }
    }

    public void cloneFieldToCard(AbstractCard c) {
        this.type = c.type;
        this.rarity = c.rarity;
        this.target = c.target;
        this.isMultiDamage = ReflectionHacks.getPrivate(c, AbstractCard.class, "isMultiDamage");

        this.isInnate = c.isInnate;
        this.selfRetain = c.selfRetain;
        this.isEthereal = c.isEthereal;
        this.exhaust = c.exhaust;

        this.costForTurn = c.costForTurn;
        this.cost = c.cost;
        this.isCostModified = c.isCostModified;
        this.isCostModifiedForTurn = c.isCostModifiedForTurn;

        this.freeToPlayOnce = c.freeToPlayOnce;
    }

    public void onFlipInHand(boolean isBack) {
        if (!isBack) {
            if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).onFlipInHand(true);
            }
//        翻转后
            if (this.isFlip) {
                onThisFlipInHand();
            } else {
                if (this.backCard != null && this.backCard instanceof AbstractTSCard)
                    ((AbstractTSCard) this.backCard).onThisFlipInHand();
            }
        }
    }

    public void onThisFlipInHand() {
    }

    public void onViewingFlip() {
        if (this.rarity == CardRarity.BASIC || this.rarity == CardRarity.SPECIAL) {
            setBackCardBackground(this, ifChangeToBackSide());
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.backCard != null) {
            this.backCard.freeToPlayOnce = this.freeToPlayOnce;
            this.backCard.energyOnUse = this.energyOnUse;
        }
        useCommon(p, m);


        if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
            ((AbstractTSCard) this.backCard).useCommon(p, m);
        }

        if (canDoubleTrigger()) {
            doubleOnUseOnce = false;

            if (this.backCard != null) {
                this.backCard.freeToPlayOnce = true;
            }

            if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
                AbstractTSCard bc = (AbstractTSCard) this.backCard;
                if (bc.exhaust || exhaustOriginal) {
                    this.exhaust = true;
                } else if (bc.cardTypeOriginal == CardType.POWER || cardTypeOriginal == CardType.POWER) {
                    this.purgeOnUse = true;
                }
            }

            if (m == null) {
                m = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }
            useThisCard(p, m);

            AbstractMonster rm = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            useBackCard(p, rm);

            useCommon(p, m);

            if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).useCommon(p, m);
            }

            if (flipOnUseOnce) {
                this.isFlip = !this.isFlip;
                this.onFlip();
                flipOnUseOnce = false;
            }

            return;
        }

        if (!isFlip) {
            useThisCard(p, m);
        } else {
            useBackCard(p, m);
        }


        if (flipOnUseOnce) {
            this.isFlip = !this.isFlip;
            this.onFlip();
            flipOnUseOnce = false;
        }
    }

    public boolean canDoubleTrigger() {
        if (doubleOnUseOnce)
            return true;

        if (AbstractDungeon.player.hasPower(AnnihilatePower.POWER_ID)) {
            return true;
        }

        if (AbstractDungeon.player.hasPower(RealityFormPower.POWER_ID) && !purgeOnUse) {
            RealityFormPower p = (RealityFormPower) AbstractDungeon.player.getPower(RealityFormPower.POWER_ID);
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() <= p.amount) {
                return true;
            }
        }


        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (!isFlip) {
            EndTurnPlayThisCard();
        } else {
            EndTurnPlayBackCard();
        }
    }


    public void EndTurnPlayThisCard() {
    }

    public void EndTurnPlayBackCard() {
        if (this.backCard != null) {
            if (this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).EndTurnPlayThisCard();
            } else {
                this.backCard.triggerOnEndOfTurnForPlayingCard();
            }
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (this.backCard != null)
            this.backCard.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (this.backCard != null)
            this.backCard.calculateCardDamage(mo);
    }

    //    true 是背面
    public boolean ifChangeToBackSide() {
        return isFlip != isViewingFlip;
    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
    }

    public void useBackCard(AbstractPlayer p, AbstractMonster m) {
        if (this.backCard != null) {
            if (this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).useThisCard(p, m);
            } else {
                this.backCard.use(p, m);
            }
        }
    }

    public void useCommon(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.isViewingFlip) {
            this.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:ViewingFlip").TEXT[0];
            return false;
        }

        if (this.isFlip && this.backCard != null) {
            return this.backCard.canUse(p, m);
        }

        return super.canUse(p, m);
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (this.isViewingFlip) {
            this.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:ViewingFlip").TEXT[0];
            return false;
        }

        return super.canPlay(card);
    }

    @Override
    public boolean canUpgrade() {
        if (this.backCard != null) {
            return !this.upgraded || !this.backCard.upgraded;
        }

        return super.canUpgrade();
    }

    public void initializeBackCard() {
        if (backCard == null && AbstractDungeon.player != null && AbstractDungeon.cardRandomRng != null) {
            if (this.rarity == CardRarity.BASIC || this.rarity == CardRarity.SPECIAL) {
                this.backCard = this;
                return;
            }

            switch (AbstractDungeon.rollRarity()) {
                case COMMON:
                    backCardIndex = AbstractDungeon.cardRng.random(22);
                    break;
                case UNCOMMON:
                    backCardIndex = AbstractDungeon.cardRng.random(23, 53);
                    break;
                case RARE:
                    backCardIndex = AbstractDungeon.cardRng.random(54, 70);
                    break;
            }

            setBackCardFromIndex(backCardIndex);

//            战斗中不印Healing
            if (CardCrawlGame.dungeon != null)
                if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT)
                    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        while (this.backCard.hasTag(CardTags.HEALING)) {
                            switch (AbstractDungeon.rollRarity()) {
                                case COMMON:
                                    backCardIndex = AbstractDungeon.cardRng.random(22);
                                    break;
                                case UNCOMMON:
                                    backCardIndex = AbstractDungeon.cardRng.random(23, 53);
                                    break;
                                case RARE:
                                    backCardIndex = AbstractDungeon.cardRng.random(54, 70);
                                    break;
                            }

                            setBackCardFromIndex(backCardIndex);
                        }
                    }


            if (this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).onInitializeBackCard(this);
            }
        }
    }

    public void onInitializeBackCard(AbstractCard thisCard) {

    }


    public void setBackCardFromIndex(int index) {
        try {
            backCard = TheShadowMod.shadowCardPool.get(index).getClass().newInstance();
            setBackCardBackground((AbstractTSCard) backCard, true);

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void setBackCardBackground(AbstractTSCard c, boolean isViewingBack) {
        if (isViewingBack) {
            switch (c.type) {
                case ATTACK:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_attack_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_attack_lime2.png");
                    break;
                case POWER:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_power_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_power_lime2.png");
                    break;
                default:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_skill_lime2.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_skill_lime2.png");
                    break;
            }
        } else {
            switch (c.type) {
                case ATTACK:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_attack_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_attack_lime.png");
                    break;
                case POWER:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_power_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_power_lime.png");
                    break;
                default:
                    c.setBackgroundTexture("TheShadowMod/img/cardui/TheShadow/512/bg_skill_lime.png",
                            "TheShadowMod/img/cardui/TheShadow/1024/bg_skill_lime.png");
                    break;
            }
        }
    }

    public static void setBackCardBackground(AbstractTSCard c) {
        setBackCardBackground(c, false);
    }


    @Override
    public AbstractCard makeCopy() {
        AbstractTSCard c = (AbstractTSCard) super.makeCopy();
        c.initializeBackCard();
        c.thisCopy = c;
        return c;
    }


    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard bcSave = backCard;
        AbstractTSCard c = (AbstractTSCard) super.makeStatEquivalentCopy();

        if (bcSave instanceof AbstractTSCard) {
            try {
                c.backCard = bcSave.getClass().newInstance();
                setBackCardBackground((AbstractTSCard) c.backCard, true);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

            c.backCardIndex = backCardIndex;
            AbstractTSCard card = (AbstractTSCard) c.backCard;

            for (int i = 0; i < backCard.timesUpgraded; i++) {
                card.upgrade();
            }

            card.secondaryM = ((AbstractTSCard) this.backCard).secondaryM;
            card.baseSecondaryM = ((AbstractTSCard) this.backCard).baseSecondaryM;
            card.upgradeSecondaryM = ((AbstractTSCard) this.backCard).upgradeSecondaryM;
            card.isSecondaryMModified = ((AbstractTSCard) this.backCard).isSecondaryMModified;

            card.name = this.backCard.name;
            card.target = this.backCard.target;
            card.upgraded = this.backCard.upgraded;
            card.timesUpgraded = this.backCard.timesUpgraded;
            card.baseDamage = this.backCard.baseDamage;
            card.baseBlock = this.backCard.baseBlock;
            card.baseMagicNumber = this.backCard.baseMagicNumber;
            card.cost = this.backCard.cost;
            card.costForTurn = this.backCard.costForTurn;
            card.isCostModified = this.backCard.isCostModified;
            card.isCostModifiedForTurn = this.backCard.isCostModifiedForTurn;
            card.inBottleLightning = this.backCard.inBottleLightning;
            card.inBottleFlame = this.backCard.inBottleFlame;
            card.inBottleTornado = this.backCard.inBottleTornado;
            card.isSeen = this.backCard.isSeen;
            card.isLocked = this.backCard.isLocked;
            card.misc = this.backCard.misc;
            card.freeToPlayOnce = this.backCard.freeToPlayOnce;

        } else if (bcSave != null) {
            c.backCard = bcSave.makeStatEquivalentCopy();
        }
        return c;
    }

    @Override
    public Integer onSave() {
        if (backCard == null) {
            initializeBackCard();
        }

        if (backCardIndex < 0) return backCard.timesUpgraded * 100;

        return backCard.timesUpgraded * 100 + backCardIndex;
    }

    @Override
    public void onLoad(Integer integer) {
        if (backCard == null) {
            initializeBackCard();
        }

        if (this.rarity == CardRarity.BASIC || this.rarity == CardRarity.SPECIAL) {
            backCard = this;
            return;
        }

        backCardIndex = integer % 100;
        backCard = TheShadowMod.shadowCardPool.get(backCardIndex).makeCopy();

        if (integer / 100 > 0) {
            for (int i = 0; i < integer / 100; i++) {
                backCard.upgrade();
            }
        }
    }

    @Override
    public Type savedType() {
        return Integer.class;
    }


    @SpireOverride
    protected void renderCard(SpriteBatch sb, boolean hovered, boolean selected) {
        if (ifChangeToBackSide() && (this.backCard != this)) {
            if (!Settings.hideCards) {
                if (this.current_y >= -200.0F * Settings.scale && this.current_y <= (float) Settings.HEIGHT + 200.0F * Settings.scale) {
                    this.backCard.current_x = this.current_x;
                    this.backCard.current_y = this.current_y;
                    this.backCard.targetDrawScale = this.targetDrawScale;
                    this.backCard.drawScale = this.drawScale;

                    this.backCard.targetAngle = this.targetAngle;
                    this.backCard.angle = this.angle;

                    this.backCard.render(sb);
                }
            }
        } else {
            SpireSuper.call(sb, hovered, selected);
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        super.renderCardTip(sb);


        if (!Settings.hideCards && (boolean) ReflectionHacks.getPrivate(this, AbstractCard.class, "renderTip"))
            if (this.backCard != null && this.backCard != this) {
                renderCardPreviewBack(sb);
            }
    }

    public void renderCardPreviewBack(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen)
            return;

        if (!ifChangeToBackSide()) {
            if (this.current_x > Settings.WIDTH * 0.75F) {
                this.backCard.current_x = this.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
            } else {
                this.backCard.current_x = this.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
            }

            if (this.cardsToPreview == null) {
                this.backCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
            } else {
                if (this.current_y < Settings.HEIGHT * 0.5F) {
                    this.backCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F + IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
                } else {
                    this.backCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT * 1.5F * 0.8F) * this.drawScale;
                }
            }

            this.backCard.drawScale = this.drawScale * 0.8F;
            this.backCard.setAngle(0.0f, true);

            this.backCard.render(sb);

        } else {
            if (this.thisCopy != null) {
                AbstractCard renderCard = this.thisCopy.makeStatEquivalentCopy();
                renderCard.targetDrawScale = this.targetDrawScale * 0.8f;
                renderCard.drawScale = this.drawScale * 0.8f;

                renderCard.setAngle(0.0f);

                if (this.current_x > Settings.WIDTH * 0.75F) {
                    renderCard.current_x = this.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
                } else {
                    renderCard.current_x = this.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
                }

                if (this.cardsToPreview == null) {
                    renderCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
                } else {
                    if (this.current_y < Settings.HEIGHT * 0.5F) {
                        renderCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F + IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
                    } else {
                        renderCard.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT * 1.5F * 0.8F) * this.drawScale;
                    }
                }

                renderCard.render(sb);


            }
        }
    }

    public void renderCardPreviewBackInSingleView(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        if (!ViewFlipButton.isViewingFlip) {
            this.backCard.current_x = Settings.WIDTH - 1435.0F * Settings.scale;
            this.backCard.current_y = Settings.HEIGHT - 795.0F * Settings.scale;
            this.backCard.drawScale = 0.8F;
            this.backCard.render(sb);
        } else {
            this.thisCopy.current_x = Settings.WIDTH - 1435.0F * Settings.scale;
            this.thisCopy.current_y = Settings.HEIGHT - 795.0F * Settings.scale;
            this.thisCopy.drawScale = 0.8F;
            this.thisCopy.render(sb);
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class InitializeDeckPatch {
        @SpireInsertPatch(rloc = 1036 - 1029, localvars = {"c", "placeOnTop"})
        public static void Insert(CardGroup _instance, CardGroup masterDeck,
                                  AbstractCard c, ArrayList<AbstractCard> placeOnTop) {
            if (c instanceof AbstractTSCard) {
                AbstractTSCard tsc = (AbstractTSCard) c;
                if (tsc.backCard != null && tsc.backCard.isInnate) {
                    placeOnTop.add(tsc);
                }
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderTips"
    )
    public static class RenderCardPreviewBackInSingleView {
        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup _instance, SpriteBatch sb) {
            if (!ViewFlipButton.isViewingFlip) {
                AbstractCard card = ReflectionHacks.getPrivate(_instance, SingleCardViewPopup.class, "card");
                if (card instanceof AbstractTSCard && ((AbstractTSCard) card).backCard != null) {
                    ((AbstractTSCard) card).renderCardPreviewBackInSingleView(sb);
                }
            } else {
                if (ViewFlipButton.copyToSave instanceof AbstractTSCard) {
                    ((AbstractTSCard) ViewFlipButton.copyToSave).renderCardPreviewBackInSingleView(sb);
                }
            }
        }
    }


    @Override
    public void tookDamage() {
        if (this.isFlip && this.backCard != null) {
            this.backCard.tookDamage();
        }
    }

    @Override
    public void upgrade() {
        if (!isFlip) {
            thisUpgrade();
        } else {
            if (this.backCard != null) {
                if (this.backCard instanceof AbstractTSCard) {
                    ((AbstractTSCard) this.backCard).thisUpgrade();
                } else {
                    this.backCard.upgrade();
                }
            }
        }
    }

    public void thisUpgrade() {
    }


    @Override
    public void update() {
        super.update();

        if (this.backCard instanceof AbstractTSCard) {
            ((AbstractTSCard) this.backCard).betterUpdate(this);
        }
    }

    public void betterUpdate(AbstractCard thisCard) {
    }
}
