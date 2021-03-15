package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

/**
 * Actor muro.
 */
public class WallActor extends CollisionableActor {
    /**
     * Textura del actor.
     */
    private final TextureAtlas.AtlasRegion texture;

    private final LaPandemia game;
    private float elapsedTime;

    public WallActor(final LaPandemia game, String regionName) {
        this.game = game;
        this.texture = ((TextureAtlas)game.getAssetManager().get("images.pack")).findRegion(regionName);

        elapsedTime = 0;

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();

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