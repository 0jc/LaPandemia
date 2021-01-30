package com.colegiovivas.lapandemia.actors.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.GenerableActor;
import com.colegiovivas.lapandemia.actors.PlayerActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class ActorGenerator {
    private static final float SAFE_DISTANCE = 400;

    private final float tick;
    private final float width;
    private final float height;
    private final Float maxCount;
    private final Pool<GenerableActor> generableActorPool;

    private LaPandemia game;
    private GameScreen gameScreen;

    private int count;
    private float lastActorTime;

    public ActorGenerator(final float tick, final float width, final float height, final Float maxCount,
                          final Pool<GenerableActor> generableActorPool) {
        this.tick = tick;
        this.width = width;
        this.height = height;
        this.maxCount = maxCount;
        this.generableActorPool = generableActorPool;
    }

    public Pool<GenerableActor> getPool() {
        return generableActorPool;
    }

    public void remove(GenerableActor actor) {
        count--;
        generableActorPool.free(actor);
    }

    public void render(float delta) {
        if (maxCount != null && count == maxCount) {
            lastActorTime = 0;
        } else {
            lastActorTime += delta;
            if (lastActorTime >= tick) {
                Rectangle rect = game.rectPool.obtain();
                rect.set(0, 0, width, height);
                try {
                    if (tryAssignCoords(rect)) {
                        lastActorTime = 0;
                        GenerableActor actor = generableActorPool.obtain().init(this, rect.x, rect.y);
                        actor.setCollisionDispatcher(gameScreen.getCollisionDispatcher());
                        gameScreen.getStage().addActor(actor);
                        count++;
                    }
                } finally {
                    game.rectPool.free(rect);
                }
            }
        }
    }

    private boolean tryAssignCoords(Rectangle outCoords) {
        // No generamos coordenadas demasiado cerca de los bordes del mapa, donde de
        // todos modos es improbable que no haya muros en cualquier nivel.
        outCoords.x = MathUtils.random(32, gameScreen.getWorldWidth() - 32 - 64);
        outCoords.y = MathUtils.random(32, gameScreen.getWorldHeight() - 32 - 64);

        Rectangle actorRect = game.rectPool.obtain();
        try {
            for (Actor actor : gameScreen.getStage().getActors()) {
                if (actor instanceof PlayerActor) {
                    actorRect.x = actor.getX() - SAFE_DISTANCE;
                    actorRect.y = actor.getY() - SAFE_DISTANCE;
                    actorRect.width = 2 * SAFE_DISTANCE + actor.getWidth();
                    actorRect.height = 2 * SAFE_DISTANCE + actor.getHeight();
                } else {
                    actorRect.x = actor.getX();
                    actorRect.y = actor.getY();
                    actorRect.width = actor.getWidth();
                    actorRect.height = actor.getHeight();
                }

                if (actorRect.overlaps(outCoords)) {
                    return false;
                }
            }
        } finally {
            game.rectPool.free(actorRect);
        }
        return true;
    }

    public void setGame(LaPandemia game) {
        this.game = game;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
