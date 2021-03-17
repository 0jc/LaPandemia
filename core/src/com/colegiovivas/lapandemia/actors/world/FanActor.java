package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

/**
 * Actor ventilador.
 */
public class FanActor extends CollisionableActor {
    /**
     * Animación del actor.
     */
    private final Animation<TextureRegion> animation;

    /**
     * Tiempo transcurrido desde el principio de la existencia del actor.
     */
    private float elapsedTime;

    /**
     * Inicializa el actor.
     * @param assetManager Gestor de recursos de la aplicación.
     * @param regionName Nombre del sprite del actor indicado en el archivo JSON del mapa.
     * @param frameDuration Valor frameDuration indicado en el archivo JSON del mapa.
     */
    public FanActor(AssetManager assetManager, String regionName, float frameDuration) {
        this.animation = new Animation<TextureRegion>(frameDuration,
                ((TextureAtlas)assetManager.get("images.pack")).findRegions(regionName));

        setWidth(64);
        setHeight(64);
        elapsedTime = 0;

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += getWorld().isPaused() ? 0 : Gdx.graphics.getDeltaTime();

        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                getX(), getY());
    }

    @Override
    public ActorId getActorId() {
        return ActorId.FAN;
    }
}
