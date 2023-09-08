package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.AbstractShadowModCard;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.helpers.ViewFlipButton;
import TheShadowMod.patches.CardColorEnum;
import TheShadowMod.powers.TheShadow.AnnihilatePower;
import TheShadowMod.powers.TheShadow.RealityFormPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public abstract class AbstractTSCard extends AbstractShadowModCard {
    public AbstractCard backCard;
    public AbstractCard thisCopy;

    public boolean isViewingFlip = false;
    public boolean isFlip = false;
    public boolean flipOnUseOnce = false;
    public boolean doubleOnUseOnce = false;

    public AbstractTSCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, img, cost, type, rarity, target);
        this.color = CardColorEnum.TheShadow_LIME;
    }


    public void onFlipView() {
        if (this.isFlip) {
            this.thisCopy = this.makeStatEquivalentCopy();
            ((AbstractTSCard) this.thisCopy).backCard = null;

            if (this.backCard != null) {
                BackCardManager.cloneFieldToCard(this, this.backCard);
            }
        } else {
            if (this.thisCopy != null) {
                BackCardManager.cloneFieldToCard(this, this.thisCopy);
            }
        }

        onViewingFlip();
    }


    public void onFlip(AbstractTSCard thisCard,boolean flipThisSide) {

    }

    public void onFlipInHand(AbstractTSCard thisCard, boolean flipThisSide) {
    }


    public void onViewingFlip() {
        if (this.backCard instanceof AbstractTSCard && this.thisCopy instanceof AbstractTSCard)
            if (this.backCard.rarity == CardRarity.BASIC && this.thisCopy.rarity == CardRarity.BASIC) {
                setBackCardBackground(this, ifChangeToBackSide());
            }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.backCard != null) {
            this.backCard.freeToPlayOnce = this.freeToPlayOnce;
            this.backCard.energyOnUse = this.energyOnUse;
        }

        if (this.thisCopy != null) {
            this.thisCopy.freeToPlayOnce = this.freeToPlayOnce;
            this.thisCopy.energyOnUse = this.energyOnUse;
        }

        useCommon(p, m);

        if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
            ((AbstractTSCard) this.backCard).useCommon(p, m);
        }

        if (canDoubleTrigger()) {
            doubleOnUseOnce = false;

            useCommon(p, m);

            if (this.backCard != null && this.backCard instanceof AbstractTSCard) {
                ((AbstractTSCard) this.backCard).useCommon(p, m);
            }


            if (!isFlip) {
                if (this.backCard != null) {
                    this.backCard.freeToPlayOnce = true;
                }
            } else {
                if (this.thisCopy != null) {
                    this.thisCopy.freeToPlayOnce = true;
                }
            }


            if (m == null) {
                m = (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }

            if (this.isFlip) {
                useBackCard(p, m);
                useThisCard(p, m);
            } else {
                useThisCard(p, m);
                useBackCard(p, m);
            }


            if (flipOnUseOnce) {
                this.isFlip = !this.isFlip;
                this.onFlipView();
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
            this.onFlipView();
            flipOnUseOnce = false;
        }
    }


    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class BackPowerCardPowerPatch {
        @SpireInsertPatch(rloc = 25)
        public static SpireReturn<Void> Insert(UseCardAction _instance) {
            AbstractCard targetCard = ReflectionHacks.getPrivate(_instance, UseCardAction.class, "targetCard");


            boolean isPower = (targetCard.type == CardType.POWER);

            if (targetCard instanceof AbstractTSCard) {
                AbstractTSCard c = (AbstractTSCard) targetCard;
                if (c.canDoubleTrigger()) {
                    if (c.backCard != null && c.backCard.type == AbstractCard.CardType.POWER) {
                        isPower = true;
                    }
                    if (c.thisCopy != null && c.thisCopy.type == AbstractCard.CardType.POWER) {
                        isPower = true;
                    }
                } else {
                    if (c.isFlip) {
                        if (c.backCard != null && c.backCard.type == AbstractCard.CardType.POWER) {
                            isPower = true;
                        }
                    } else {
                        if (c.thisCopy != null && c.thisCopy.type == AbstractCard.CardType.POWER) {
                            isPower = true;
                        }
                    }
                }
            }


            if (isPower) {
                AbstractDungeon.actionManager.addToTop(new ShowCardAction(targetCard));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.7F));
                }
                AbstractDungeon.player.hand.empower(targetCard);
                _instance.isDone = true;
                AbstractDungeon.player.hand.applyPowers();
                AbstractDungeon.player.hand.glowCheck();
                AbstractDungeon.player.cardInUse = null;

                return SpireReturn.Return();
            }


            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, AbstractCreature.class}
    )
    public static class BackPowerCardExhaustPatch {
        @SpireInsertPatch(rloc = 25)
        public static SpireReturn<Void> Insert(UseCardAction _instance, AbstractCard card, AbstractCreature target) {
            if (card instanceof AbstractTSCard) {
                AbstractTSCard c = (AbstractTSCard) card;
                if (!c.canDoubleTrigger()) {
                    if (c.isFlip) {
                        if (c.backCard != null) {
                            if (c.backCard.exhaust || c.backCard.exhaustOnUseOnce) {
                                _instance.exhaustCard = true;
                            }
                        }
                    } else {
                        if (c.thisCopy != null) {
                            if (c.thisCopy.exhaust || c.thisCopy.exhaustOnUseOnce) {
                                _instance.exhaustCard = true;
                            }
                        }
                    }
                } else {
                    if (c.backCard != null) {
                        if (c.backCard.exhaust || c.backCard.exhaustOnUseOnce) {
                            _instance.exhaustCard = true;
                        }
                    }

                    if (c.thisCopy != null) {
                        if (c.thisCopy.exhaust || c.thisCopy.exhaustOnUseOnce) {
                            _instance.exhaustCard = true;
                        }
                    }
                }
            }

            return SpireReturn.Continue();
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

        if (this.thisCopy != null && this.thisCopy != this)
            this.thisCopy.applyPowers();

        if (this.backCard != null && this.backCard != this)
            this.backCard.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);

        if (this.thisCopy != null && this.thisCopy != this)
            this.thisCopy.calculateCardDamage(mo);

        if (this.backCard != null && this.backCard != this)
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

        if (this.isFlip && this.backCard != null && !this.backCard.cardID.equals(this.cardID)) {
            return this.backCard.canUse(p, m);
        }

        return super.canUse(p, m);
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        AbstractTSCard t = (AbstractTSCard) card;
        if (t.isViewingFlip) {
            this.cantUseMessage = CardCrawlGame.languagePack.getUIString("TheShadowMod:ViewingFlip").TEXT[0];
            return false;
        }

        if (t.isFlip && t.backCard != null && !t.backCard.cardID.equals(t.cardID)) {
            return t.backCard.canPlay(t.backCard);
        }


        return super.canPlay(card);
    }

    @Override
    public boolean canUpgrade() {
        if (this.backCard != null) {
            return super.canUpgrade() || this.backCard.canUpgrade();
        }

        return super.canUpgrade();
    }

    public void initializeBackCard() {
        if (backCard == null && AbstractDungeon.player != null && AbstractDungeon.cardRandomRng != null) {
            if (this.rarity == CardRarity.BASIC) {
                this.backCard = this;
                return;
            }

            int index = 0;

            if (CardCrawlGame.dungeon != null) {
                if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT)
                    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        while (TheShadowMod.shadowCardPool.get(index).hasTag(AbstractCard.CardTags.HEALING)) {
                            switch (AbstractDungeon.rollRarity()) {
                                case COMMON:
                                    index = AbstractDungeon.cardRng.random(22);
                                    break;
                                case UNCOMMON:
                                    index = AbstractDungeon.cardRng.random(23, 53);
                                    break;
                                case RARE:
                                    index = AbstractDungeon.cardRng.random(54, 70);
                                    break;
                            }
                        }

                        setBackCardFromID(this, TheShadowMod.shadowCardPool.get(index).cardID, 0, TheShadowMod.shadowCardPool.get(index).misc);
                        return;
                    }
            }

            switch (AbstractDungeon.rollRarity()) {
                case COMMON:
                    index = AbstractDungeon.cardRng.random(22);
                    break;
                case UNCOMMON:
                    index = AbstractDungeon.cardRng.random(23, 53);
                    break;
                case RARE:
                    index = AbstractDungeon.cardRng.random(54, 70);
                    break;
            }

            setBackCardFromID(this, TheShadowMod.shadowCardPool.get(index).cardID, 0, TheShadowMod.shadowCardPool.get(index).misc);
        }
    }

    public static boolean noLoop = false;

    public void setBackCardFromID(AbstractCard card, String id, int upgrades, int misc) {
        if (!noLoop) {
            noLoop = true;
            AbstractCard c = CardLibrary.getCopy(id, upgrades, misc);
            if (c instanceof AbstractTSCard) {
                setBackCardBackground((AbstractTSCard) c, true);
            }

            BackCardManager.setCardToBackCard(c, card, true);
            noLoop = false;
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


    @SpireOverride
    protected void renderCard(SpriteBatch sb, boolean hovered, boolean selected) {
        if (!Settings.hideCards) {
            if (ifChangeToBackSide()) {
                if (this.backCard != null && !this.backCard.cardID.equals(this.cardID)) {
                    if (this.current_y >= -200.0F * Settings.scale && this.current_y <= (float) Settings.HEIGHT + 200.0F * Settings.scale) {
                        this.backCard.current_x = this.current_x;
                        this.backCard.current_y = this.current_y;
                        this.backCard.targetDrawScale = this.targetDrawScale;
                        this.backCard.drawScale = this.drawScale;

                        this.backCard.targetAngle = this.targetAngle;
                        this.backCard.angle = this.angle;

                        this.backCard.render(sb);
                        return;
                    }
                }
            } else {
                if (this.thisCopy != null && this.thisCopy != this) {
                    if (this.current_y >= -200.0F * Settings.scale && this.current_y <= (float) Settings.HEIGHT + 200.0F * Settings.scale) {
                        this.thisCopy.current_x = this.current_x;
                        this.thisCopy.current_y = this.current_y;
                        this.thisCopy.targetDrawScale = this.targetDrawScale;
                        this.thisCopy.drawScale = this.drawScale;

                        this.thisCopy.targetAngle = this.targetAngle;
                        this.thisCopy.angle = this.angle;

                        this.thisCopy.render(sb);
                        return;
                    }
                }
            }
        }


        SpireSuper.call(sb, hovered, selected);
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        if (!Settings.hideCards && (boolean) ReflectionHacks.getPrivate(this, AbstractCard.class, "renderTip")) {
            if (this.backCard != null && this.backCard != this) {
                renderCardPreviewBack(sb);
            }
        }

        if (!ifChangeToBackSide())
            super.renderCardTip(sb);

    }

    public void renderCardPreviewBack(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen)
            return;

        if (!ifChangeToBackSide()) {
            if (this.backCard != null && !this.backCard.cardID.equals(this.cardID)) {
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

                this.backCard.hb.move(this.backCard.current_x, this.backCard.current_y);
                this.backCard.hb.resize(
                        (float) ReflectionHacks.getPrivate(this.backCard, AbstractCard.class, "HB_W") * this.backCard.drawScale,
                        (float) ReflectionHacks.getPrivate(this.backCard, AbstractCard.class, "HB_H") * this.backCard.drawScale
                );

                this.backCard.render(sb);
            }

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

                this.thisCopy.hb.move(this.thisCopy.current_x, this.thisCopy.current_y);
                this.thisCopy.hb.resize(
                        (float) ReflectionHacks.getPrivate(this.thisCopy, AbstractCard.class, "HB_W") * this.thisCopy.drawScale,
                        (float) ReflectionHacks.getPrivate(this.thisCopy, AbstractCard.class, "HB_H") * this.thisCopy.drawScale
                );


                renderCard.render(sb);
            }
        }
    }

    public void renderCardPreviewBackInSingleView(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        if (!ViewFlipButton.isViewingFlip) {
            if (this.backCard != null && !this.backCard.cardID.equals(this.cardID)) {
                this.backCard.current_x = Settings.WIDTH - 1435.0F * Settings.scale;
                this.backCard.current_y = Settings.HEIGHT - 795.0F * Settings.scale;
                this.backCard.drawScale = 0.8F;
                this.backCard.render(sb);
            }
        } else {
            if (this.thisCopy != null && this.backCard != null && !this.thisCopy.cardID.equals(this.backCard.cardID)) {
                this.thisCopy.current_x = Settings.WIDTH - 1435.0F * Settings.scale;
                this.thisCopy.current_y = Settings.HEIGHT - 795.0F * Settings.scale;
                this.thisCopy.drawScale = 0.8F;
                this.thisCopy.render(sb);
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
    public void upgrade() {
        if (!isFlip) {
            if (this.thisCopy != null) {
                if (this.thisCopy instanceof AbstractTSCard) {
                    ((AbstractTSCard) this.thisCopy).thisUpgrade();

                } else {
                    this.backCard.upgrade();
                }

                if (this.thisCopy != this) {
                    BackCardManager.cloneFieldToCard(this, this.thisCopy);
                }
            }
        } else {
            if (this.backCard != null) {
                if (this.backCard instanceof AbstractTSCard) {
                    ((AbstractTSCard) this.backCard).thisUpgrade();
                } else {
                    this.backCard.upgrade();
                }
                if (this.backCard != this) {
                    BackCardManager.cloneFieldToCard(this, this.backCard);
                }
            }
        }
    }

    public void thisUpgrade() {
    }


    @Override
    public void update() {
        super.update();

        if (this.thisCopy != null && this.thisCopy != this) {
            this.thisCopy.update();
            if (this.thisCopy instanceof AbstractTSCard)
                ((AbstractTSCard) this.thisCopy).betterUpdate(this);
        }


        if (this.backCard != null && this.backCard != this) {
            this.backCard.update();
            if (this.backCard instanceof AbstractTSCard)
                ((AbstractTSCard) this.backCard).betterUpdate(this);
        }
    }

    public void betterUpdate(AbstractCard thisCard) {
    }

    public void cloneFieldCommon(AbstractCard ori) {
    }

    @Override
    public void updateCost(int amt) {
        super.updateCost(amt);

        if (this.thisCopy != null && this.thisCopy != this) {
            this.thisCopy.updateCost(amt);
        }

        if (this.backCard != null && this.backCard != this) {
            this.backCard.updateCost(amt);
        }

    }

    @Override
    public void setCostForTurn(int amt) {
        super.setCostForTurn(amt);

        if (this.thisCopy != null && this.thisCopy != this) {
            this.thisCopy.setCostForTurn(amt);
        }

        if (this.backCard != null && this.backCard != this) {
            this.backCard.setCostForTurn(amt);
        }

    }

    @Override
    public void modifyCostForCombat(int amt) {
        super.setCostForTurn(amt);

        if (this.thisCopy != null && this.thisCopy != this) {
            this.thisCopy.modifyCostForCombat(amt);
        }

        if (this.backCard != null && this.backCard != this) {
            this.backCard.modifyCostForCombat(amt);
        }

    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "resetAttributes"
    )
    public static class ResetAttributesPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _instance) {
            if (_instance instanceof AbstractTSCard) {
                AbstractTSCard card = (AbstractTSCard) _instance;
                if (card.thisCopy != null && card.thisCopy != card) {
                    card.thisCopy.resetAttributes();
                }

                if (card.backCard != null && card.backCard != card) {
                    card.backCard.resetAttributes();
                }
            }
        }
    }


}
