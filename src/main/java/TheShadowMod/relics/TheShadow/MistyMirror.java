package TheShadowMod.relics.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.relics.AbstractShadowModRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MistyMirror extends AbstractShadowModRelic implements NotDeadRelic {
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
    public boolean onDead(AbstractPlayer player, DamageInfo damageInfo) {
        if ((AbstractDungeon.getCurrRoom()).monsters != null && !(AbstractDungeon.getCurrRoom()).monsters
                .areMonstersBasicallyDead() && AbstractDungeon.actionManager.turnHasEnded) {
            return true;
        }


        addToTop(new RelicAboveCreatureAction(player, this));
        flash();
        player.heal(player.maxHealth / 2);
        addToTop(new ApplyPowerAction(player,player,new VulnerablePower(player,1,false),1));
        return false;
    }
}