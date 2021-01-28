package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;

public class MaskActor extends Actor implements Pool.Poolable {
    private final Texture texture;
    private final LaPandemia game;

    public MaskActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("mask.png");

        setTouchable(Touchable.enabled);
    }

    public MaskActor init(float x, float y) {
        setBounds(x, y, 64, 32);
        return this;
    }

    @Override
    public void reset() {
        setPosition(0, 0);
    }

    @Override
    public boolean remove() {
        game.maskPool.free(this);
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
    }
}
