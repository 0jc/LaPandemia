package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

public class FanActor extends CollisionableActor {
    private final Animation<TextureRegion> animation;
    private final LaPandemia game;
    private float elapsedTime;

    public FanActor(final LaPandemia game, String regionName, float frameDuration) {
        this.game = game;
        this.animation = new Animation<TextureRegion>(frameDuration,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions(regionName));

        setWidth(64);
        setHeight(64);
        elapsedTime = 0;

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                getX(), getY());
    }

    @Override
    public ActorId getActorId() {
        return ActorId.FAN;
    }
}
