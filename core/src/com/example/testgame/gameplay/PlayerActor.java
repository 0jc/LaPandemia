package com.example.testgame.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class PlayerActor extends Actor {
    private final float WORLD_W;
    private final float WORLD_H;

    private final Animation<TextureRegion> noMasksAnimation;
    private final Animation<TextureRegion> fewMasksAnimation;
    private final Animation<TextureRegion> manyMasksAnimation;

    // -1: izquierda/abajo, 0: quieto, 1: derecha/arriba.
    private int xDir;
    private int yDir;

    // Tiempo transcurrido desde la última llamada a draw(), necesario para
    // animar el sprite.
    private float elapsedTime;
    private float speed;

    // Mascarillas recolectadas.
    private int maskCount;

    public PlayerActor(float x, float y, final AssetManager assetManager, final float WORLD_W, final float WORLD_H) {
        this.WORLD_W = WORLD_W;
        this.WORLD_H = WORLD_H;

        setBounds(x, y, 64, 64);
        setDirection(0, 0);
        elapsedTime = 0;
        speed = 400;

        noMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) assetManager.get("player-no-masks.pack")).getRegions());
        fewMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) assetManager.get("player-few-masks.pack")).getRegions());
        manyMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) assetManager.get("player-many-masks.pack")).getRegions());

        maskCount = 0;

        setTouchable(Touchable.enabled);
    }

    public void setMaskCount(int maskCount) {
        this.maskCount = maskCount;
    }

    public int getMaskCount() {
        return maskCount;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDirection(int xDir, int yDir) {
        if (xDir < -1 || xDir > 1 || yDir < -1 || yDir > 1) {
            throw new IllegalArgumentException();
        }

        this.xDir = xDir;
        this.yDir = yDir;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> animation =
                maskCount == 0
                ? noMasksAnimation
                : (maskCount < 3) ? fewMasksAnimation : manyMasksAnimation;
        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                getX(), getY());
    }

    @Override
    public void act(float delta) {
        setPosition(getX() + speed * delta * xDir, getY() + speed * delta * yDir);
        if (getX() < 0) setX(0);
        if (getX() > WORLD_W - getWidth()) setX(WORLD_W - getWidth());
        if (getY() < 0) setY(0);
        if (getY() > WORLD_H - getHeight()) setY(WORLD_H - getHeight());
    }
}
