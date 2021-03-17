package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

/**
 * Actor muro.
 */
public class WallActor extends CollisionableActor {
    /**
     * Textura del actor.
     */
    private final TextureAtlas.AtlasRegion texture;

    /**
     * Inicializa el actor.
     * @param assetManager Gestor de recursos de la aplicación.
     * @param regionName Nombre del sprite del patrón del muro indicado en el archivo JSON del mapa.
     */
    public WallActor(AssetManager assetManager, String regionName) {
        this.texture = ((TextureAtlas)assetManager.get("images.pack")).findRegion(regionName);

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int x = 0; x < (int)getWidth(); x += 32) {
            for (int y = 0; y < (int)getHeight(); y += 32) {
                batch.draw(texture, getX() + x, getY() + y, 32, 32);
            }
        }
    }

    @Override
    public ActorId getActorId() {
        return ActorId.WALL;
    }
}