package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;

public class HealthActor extends Actor {
    private final TextureAtlas atlas;
    private final LaPandemia game;
    private PlayerActor playerActor;
    private int health;
    private boolean show;

    // Tiempo restante durante el que se muestra la salud máxima.
    private float showMaxFor;

    public HealthActor(final LaPandemia game) {
        this.game = game;
        this.atlas = ((TextureAtlas)game.assetManager.get("health.pack"));
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
            TextureAtlas.AtlasRegion region = atlas.getRegions().get(health - 1);
            batch.draw(region,
                    playerActor.getX() + playerActor.getWidth() - region.getRegionWidth()/2f,
                    playerActor.getY() + playerActor.getHeight() - region.getRegionHeight()/2f);
        }
    }
}
