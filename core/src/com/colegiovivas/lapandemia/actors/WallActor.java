package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;
import com.colegiovivas.lapandemia.levels.Wall;

public class WallActor extends CollisionableActor {
    private final Texture texture;
    private final LaPandemia game;
    private float elapsedTime;

    public WallActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("wall.png");

        elapsedTime = 0;

        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        batch.draw(texture, getX(), getY(), 0, 0, (int)getWidth(), (int)getHeight());
    }

    @Override
    public void act(float delta) {
    }
}