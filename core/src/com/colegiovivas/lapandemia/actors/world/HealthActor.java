package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.WorldActor;

/**
 * Indicador de salud del actor.
 */
public class HealthActor extends WorldActor {
    /**
     * Texturas con los diferentes niveles de salud.
     */
    private final Array<TextureAtlas.AtlasRegion> textures;
    private final LaPandemia game;

    /**
     * Personaje cuya salud se monitorea.
     */
    private PlayerActor playerActor;

    /**
     * Salud reportada por el personaje.
     */
    private int health;

    /**
     * True si y solo si se debe mostrar el indicador.
     */
    private boolean show;

    /**
     * Tiempo restante durante el que se muestra la salud máxima.
     */
    private float showMaxFor;

    public HealthActor(final LaPandemia game) {
        this.game = game;
        this.textures = ((TextureAtlas)game.getAssetManager().get("images.pack")).findRegions("health");
        health = 4;
        showMaxFor = 0;
        show = true;
    }

    /**
     * @param health Nuevo valor de salud.
     */
    public void setHealth(int health) {
        this.health = health;
        if (health == 4) {
            showMaxFor = 1;
        }
    }

    /**
     * @param show True si y solo si se debe mostrar el indicador de salud.
     */
    public void showHealth(boolean show) {
        this.show = show;
    }

    /**
     * @param playerActor El personaje principal cuya salud se monitorea.
     */
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
