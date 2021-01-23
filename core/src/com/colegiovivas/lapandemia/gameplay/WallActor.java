package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.level.Wall;

public class WallActor extends Actor {
    private final Texture texture;
    private final Wall wall;
    private final LaPandemia game;
    private float elapsedTime;

    public WallActor(final Wall wall, final LaPandemia game) {
        this.wall = wall;
        this.game = game;
        this.texture = game.assetManager.get("wall.png");

        setBounds(wall.x, wall.y, wall.w, wall.h);
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