package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

public class WallActor extends CollisionableActor {
    private final TextureAtlas.AtlasRegion texture;
    private final LaPandemia game;
    private float elapsedTime;

    public WallActor(final LaPandemia game) {
        this.game = game;
        this.texture = ((TextureAtlas)game.assetManager.get("images.pack")).findRegion("wall");

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