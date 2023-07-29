package TheShadowMod.actions.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;
import java.util.function.Consumer;

public class BetterIncreaseMiscAction extends AbstractGameAction {
    private final int miscIncrease;
    private final UUID uuid;
    private Consumer<AbstractCard> cardConsumer;

    public BetterIncreaseMiscAction(UUID targetUUID, int miscIncrease, Consumer<AbstractCard> cardConsumer) {
        this.miscIncrease = miscIncrease;
        this.uuid = targetUUID;
        this.cardConsumer = cardConsumer;
    }

    public BetterIncreaseMiscAction(UUID targetUUID, int miscIncrease) {
        this.miscIncrease = miscIncrease;
        this.uuid = targetUUID;

    }


    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                c.misc += this.miscIncrease;
                if (cardConsumer != null)
                    cardConsumer.accept(c);
            }
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
            c.misc += this.miscIncrease;
            if (cardConsumer != null)
                cardConsumer.accept(c);
        }

        this.isDone = true;

    }

}
