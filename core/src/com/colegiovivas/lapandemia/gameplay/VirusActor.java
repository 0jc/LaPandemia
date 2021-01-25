package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class VirusActor extends Actor implements Pool.Poolable {
    private final LaPandemia game;
    private final Animation<TextureRegion> animation;

    private float elapsedTime;
    private float speed;
    private boolean alive;

    public VirusActor(final LaPandemia game) {
        this.game = game;

        animation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("virus.pack")).getRegions());

        setTouchable(Touchable.enabled);
        setWidth(32);
        setHeight(64);
    }

    public VirusActor init(float x, float y) {
        setPosition(x, y);
        speed = 3;
        alive = true;
        return this;
    }

    @Override
    public void reset() {
        elapsedTime = 0;
        speed = 0;
        alive = false;
    }

    @Override
    public boolean remove() {
        game.virusPool.free(this);
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (alive) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            batch.draw(
                    animation.getKeyFrame(elapsedTime, true),
                    getX(), getY());
        }
    }

    @Override
    public void act(float delta) {
        if (!alive) {
            // El virus ha sido eliminado por otro actor en esta misma fase "act".
            return;
        }

        // Este tipo de movimiento será posiblemente remplazado por un Action.
        float xDisplacement = 0;
        float yDisplacement = 0;
        switch (MathUtils.random(1, 4)) {
            case 1:
                xDisplacement = 1;
                break;
            case 2:
                xDisplacement = -1;
                break;
            case 3:
                yDisplacement = 1;
                break;
            case 4:
                yDisplacement = -1;
                break;
        }

        xDisplacement *= speed;
        yDisplacement *= speed;

        CollisionInfo collisionInfo = game.collisionInfoPool.obtain().init(
                this, xDisplacement, yDisplacement);
        try {
            if (collisionInfo.walls.size > 0) {
                return;
            }
            if (collisionInfo.player != null) {
                collisionInfo.player.infect();
                remove();
                return;
            }
            if (collisionInfo.fans.size > 0 || collisionInfo.viruses.size > 0) {
                remove();
                return;
            }
        } finally {
            game.collisionInfoPool.free(collisionInfo);
        }

        setPosition(getX() + xDisplacement, getY() + yDisplacement);
    }
}
