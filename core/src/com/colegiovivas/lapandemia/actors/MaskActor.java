package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;

public class MaskActor extends GenerableActor {
    private final Texture texture;
    private final LaPandemia game;

    public MaskActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("mask.png");

        setTouchable(Touchable.enabled);
        setWidth(64);
        setHeight(32);
    }

    @Override
    public void reset() {
        setPosition(0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
    }
}
