package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;
import com.colegiovivas.lapandemia.levels.Fan;

public class FanActor extends CollisionableActor {
    private final Animation<TextureRegion> animation;
    private final LaPandemia game;
    private float elapsedTime;

    public FanActor(final LaPandemia game) {
        this.game = game;
        this.animation = new Animation<TextureRegion>(1f / 5f,
                ((TextureAtlas)game.assetManager.get("fan.pack")).getRegions());

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
