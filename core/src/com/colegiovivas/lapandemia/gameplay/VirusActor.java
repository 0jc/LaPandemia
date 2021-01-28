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

public class VirusActor extends GenerableActor {
    private final LaPandemia game;
    private final Animation<TextureRegion> animation;
    private static final float SPEED = 100;
    private static final float MIN_DIR_TICK = 0.7f;
    private static final float MAX_DIR_TICK = 1.5f;

    private float animationTime;
    private boolean alive;

    private float directionTime;
    private float directionTick;
    private float xDir;
    private float yDir;

    public VirusActor(final LaPandemia game) {
        this.game = game;

        animation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("virus.pack")).getRegions());

        setTouchable(Touchable.enabled);
        setWidth(32);
        setHeight(64);
    }

    public VirusActor init(GameScreen.ActorGenerator generator, float x, float y) {
        super.init(generator, x, y);
        alive = true;
        return this;
    }

    @Override
    public void reset() {
        animationTime = 0;
        directionTime = 0;
        directionTick = 0;
        xDir = 0;
        yDir = 0;
        alive = false;
    }

    @Override
    public boolean remove() {
        alive = false;
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (alive) {
            animationTime += Gdx.graphics.getDeltaTime();
            batch.draw(
                    animation.getKeyFrame(animationTime, true),
                    getX(), getY());
        }
    }

    @Override
    public void act(float delta) {
        if (!alive) {
            // El virus ha sido eliminado por otro actor en esta misma fase "act".
            return;
        }

        directionTime += delta;
        if (directionTick == 0 || directionTime >= directionTick) {
            directionTick = MathUtils.random(MIN_DIR_TICK, MAX_DIR_TICK);
            directionTime = 0;
            xDir = MathUtils.random(-1, 1);
            yDir = MathUtils.random(-1 ,1);
        }

        float xDisplacement = xDir * SPEED * delta;
        float yDisplacement = yDir * SPEED * delta;

        CollisionInfo collisionInfo = game.collisionInfoPool.obtain().init(
                this, xDisplacement, yDisplacement);
        try {
            if (collisionInfo.walls.size > 0 || collisionInfo.masks.size > 0) {
                directionTick = 0;
                return;
            }
            if (collisionInfo.player != null) {
                collisionInfo.player.infect(this);
                return;
            }
            if (collisionInfo.fans.size > 0) {
                remove();
                return;
            }
            if (collisionInfo.viruses.size > 0) {
                directionTick = 0;
                return;
            }
        } finally {
            game.collisionInfoPool.free(collisionInfo);
        }

        setPosition(getX() + xDisplacement, getY() + yDisplacement);
    }
}
