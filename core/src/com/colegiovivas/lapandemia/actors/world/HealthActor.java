package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.WorldActor;

public class HealthActor extends WorldActor {
    private final Array<TextureAtlas.AtlasRegion> textures;
    private final LaPandemia game;
    private PlayerActor playerActor;
    private int health;
    private boolean show;

    // Tiempo restante durante el que se muestra la salud máxima.
    private float showMaxFor;

    public HealthActor(final LaPandemia game) {
        this.game = game;
        this.textures = ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("health");
        health = 4;
        showMaxFor = 0;
        show = true;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health == 4) {
            showMaxFor = 1;
        }
    }

    public void showHealth(boolean show) {
        this.show = show;
    }

    public void setPlayerActor(PlayerActor playerActor) {
        this.playerActor = playerActor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (health == 4 && showMaxFor > 0) {
            showMaxFor = Math.max(0, showMaxFor - Gdx.graphics.getDeltaTime());
        }

        if (show && 0 < health && (health < 4 || showMaxFor > 0)) {
            TextureAtlas.AtlasRegion region = textures.get(health - 1);
            batch.draw(region,
                    playerActor.getX() + playerActor.getWidth() - region.getRegionWidth()/2f,
                    playerActor.getY() + playerActor.getHeight() - region.getRegionHeight()/2f);
        }
    }
}
