package TheShadowMod.relics.TheShadow;

import TheShadowMod.TheShadowMod;
import TheShadowMod.powers.TheShadow.HeavyPower;
import TheShadowMod.relics.AbstractShadowModRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BrokenMirror extends AbstractShadowModRelic implements NotDeadRelic {
    public static final String ID = TheShadowMod.makeID(BrokenMirror.class.getSimpleName());
    private static final String imgName = BrokenMirror.class.getSimpleName() + ".png";
    private static final Texture texture = new Texture(TheShadowMod.assetPath("img/relics/") + imgName);
    private static final Texture outline = new Texture(TheShadowMod.assetPath("img/relics/outline/") + imgName);

    public BrokenMirror() {
        super(ID, texture, outline, RelicTier.BOSS, AbstractRelic.LandingSound.FLAT);
        this.counter = 2;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public boolean onDead(AbstractPlayer player, DamageInfo damageInfo) {
        if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT ) {
            if( this.counter > 0) {
                addToTop(new RelicAboveCreatureAction(player, this));
                flash();
                player.heal(player.maxHealth / 2);
                this.counter--;
                return false;
            }else {
                return true;
            }
        }else {
            flash();
            player.heal(player.maxHealth / 2);
            return false;
        }
    }


    @Override
    public void obtain() {
        AbstractPlayer player = AbstractDungeon.player;
        player.relics.stream()
                .filter(r -> r instanceof MistyMirror).findFirst()
                .map(r -> player.relics.indexOf(r))
                .ifPresent(index -> instantObtain(player, index, true));

        (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
    }

    @Override
    public void atBattleStart() {
        this.counter = 2;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(MistyMirror.ID);
    }
}