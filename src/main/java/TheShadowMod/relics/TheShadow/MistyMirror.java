package TheShadowMod.relics.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.relics.AbstractShadowModRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MistyMirror extends AbstractShadowModRelic implements OnPlayerDeathRelic {
    public static final String ID = TheShadowMod.makeID(MistyMirror.class.getSimpleName());
    private static final String imgName = MistyMirror.class.getSimpleName() + ".png";
    private static final Texture texture = new Texture(TheShadowMod.assetPath("img/relics/") + imgName);
    private static final Texture outline = new Texture(TheShadowMod.assetPath("img/relics/outline/") + imgName);

    public MistyMirror() {
        super(ID, texture, outline, RelicTier.STARTER, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public boolean onPlayerDeath(AbstractPlayer player, DamageInfo damageInfo) {
        if ((AbstractDungeon.getCurrRoom()).monsters == null || (AbstractDungeon.getCurrRoom()).monsters
                .areMonstersBasicallyDead() || AbstractDungeon.actionManager.turnHasEnded ||
                (AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
            return true;
        }


        addToTop(new RelicAboveCreatureAction(player, this));
        flash();
        player.heal(player.maxHealth / 2);
        return false;
    }
}