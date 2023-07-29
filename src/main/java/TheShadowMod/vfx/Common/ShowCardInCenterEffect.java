package TheShadowMod.vfx.Common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShowCardInCenterEffect extends AbstractGameEffect {
    private AbstractCard card;
    private float startDuration;
    private boolean shownEffect = false;

    public ShowCardInCenterEffect(AbstractCard card) {
        this.card = card.makeStatEquivalentCopy();
        this.card.target_x = Settings.WIDTH * 0.5F;
        this.card.target_y = Settings.HEIGHT * 0.5F;
        this.card.drawScale = 0.01F;
        this.card.targetDrawScale = 1.0F;

        this.startDuration = 2.0f;
        this.duration = this.startDuration;

    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        if (!shownEffect && this.duration < this.startDuration - 0.25f) {
            this.shownEffect = true;
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);

            for (int i = 0; i < 90; i++) {
                AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(this.card.current_x, this.card.current_y));
            }
            for (int i = 0; i < 50; i++) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.card.current_x, this.card.current_y));
            }
        }

        if (!this.card.fadingOut && this.duration < 0.7F) {
            this.card.fadingOut = true;
        }

        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.card.shrink();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone)
            this.card.render(sb);
    }

    @Override
    public void dispose() {
    }
}
