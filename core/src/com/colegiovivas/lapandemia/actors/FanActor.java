package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.level.Fan;

public class FanActor extends Actor {
    private final Animation<TextureRegion> animation;
    private final Fan fan;
    private final LaPandemia game;
    private float elapsedTime;

    public FanActor(final Fan fan, final LaPandemia game) {
        this.fan = fan;
        this.game = game;
        this.animation = new Animation<TextureRegion>(1f / 5f,
                ((TextureAtlas)game.assetManager.get("fan.pack")).getRegions());

        setBounds(fan.x, fan.y, 64, 64);
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
    public void act(float delta) {
    }
}
