package com.example.testgame.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.example.testgame.MyTestGame;
import com.example.testgame.level.Wall;

public class WallActor extends Actor {
    private final Texture texture;
    private final Wall wall;
    private final MyTestGame game;
    private float elapsedTime;

    public WallActor(final Wall wall, final MyTestGame game) {
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