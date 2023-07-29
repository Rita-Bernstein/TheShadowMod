package TheShadowMod.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;

public class AbstractShadowModPower extends TwoAmountPower {
    public static TextureAtlas shionAtlas;

    public static void initialize() {
        shionAtlas = new TextureAtlas(Gdx.files.internal("TheShadowMod/img/powers/powers.atlas"));
    }

    protected void loadShadowRegion(String fileName) {
        this.region48 = AbstractShadowModPower.shionAtlas.findRegion("48/" + fileName);
        this.region128 = AbstractShadowModPower.shionAtlas.findRegion("128/" + fileName);
    }

    public void onNotDeath() {
    }
}
