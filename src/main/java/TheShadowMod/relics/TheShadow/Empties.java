package TheShadowMod.relics.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.cards.TheShadow.AbstractTSCard;
import TheShadowMod.helpers.BackCardManager;
import TheShadowMod.relics.AbstractShadowModRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Empties extends AbstractShadowModRelic {
    public static final String ID = TheShadowMod.makeID(Empties.class.getSimpleName());
    private static final String imgName = Empties.class.getSimpleName() + ".png";
    private static final Texture texture = new Texture(TheShadowMod.assetPath("img/relics/") + imgName);
    private static final Texture outline = new Texture(TheShadowMod.assetPath("img/relics/outline/") + imgName);


    private boolean cardSelected = true;

    public Empties() {
        super(ID, texture, outline, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
            tmp.addToTop(c);
        }

        if (tmp.size() < 2)
            return;

        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(tmp,
                2, DESCRIPTIONS[1],
                false, false, false, false);


    }


    @Override
    public void update() {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() >= 2) {
            cardSelected = true;
            AbstractCard card0 = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractCard card1 = AbstractDungeon.gridSelectScreen.selectedCards.get(1);

            AbstractDungeon.player.masterDeck.group.set(AbstractDungeon.player.masterDeck.group.indexOf(card1),
                    BackCardManager.setCardToBackCard(card0, card1, false));


            AbstractDungeon.player.masterDeck.removeCard(card0);

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

}