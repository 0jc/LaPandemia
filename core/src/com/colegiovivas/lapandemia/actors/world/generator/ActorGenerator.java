package com.colegiovivas.lapandemia.actors.world.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.GenerableActor;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class ActorGenerator {
    private static final float SAFE_DISTANCE = 400;

    private final float tick;
    private final float width;
    private final float height;
    private final Integer maxCount;
    private final Pool<GenerableActor> generableActorPool;
    private final Float ttl;
    private final ActorId actorId;
    private final Group destGroup;

    private LaPandemia game;
    private GameScreen gameScreen;

    private int count;
    private float lastActorTime;

    public ActorGenerator(final Class<? extends GenerableActor> generableActorClass, ActorId actorId,
                          Group destGroup, float width, float height, float tick, Integer maxCount, Float ttl)
    {
        this.actorId = actorId;
        this.destGroup = destGroup;
        this.tick = tick;
        this.width = width;
        this.height = height;
        this.maxCount = maxCount;
        this.ttl = ttl;
        this.generableActorPool = new Pool<GenerableActor>() {
            @Override
            protected GenerableActor newObject() {
                try {
                    return generableActorClass.getDeclaredConstructor(LaPandemia.class).newInstance(game);
                } catch (Exception e) {
                    Gdx.app.error("LaPandemia", "ActorGenerator: " + e.getMessage());
                }

                return null;
            }
        };
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
                Array<Actor> overlappedActors = game.actorArrayPool.obtain();
                rect.set(0, 0, width, height);
                try {
                    if (tryAssignCoords(rect, overlappedActors)) {
                        lastActorTime = 0;
                        GenerableActor actor = generableActorPool.obtain().init();
                        actor.setGenerator(this);
                        actor.setBounds(rect.x, rect.y, rect.width, rect.height);
                        actor.setCollisionDispatcher(gameScreen.getCollisionDispatcher());
                        actor.setTtl(ttl);
                        destGroup.addActor(actor);
                        count++;

                        for (Actor overlappedActor : overlappedActors) {
                            ((CollisionableActor)overlappedActor).collidedBy(actor);
                        }
                    }
                } finally {
                    game.rectPool.free(rect);
                    game.actorArrayPool.free(overlappedActors);
                }
            }
        }
    }

    private boolean tryAssignCoords(Rectangle outCoords, Array<Actor> overlappedActors) {
        // No generamos coordenadas demasiado cerca de los bordes del mapa, donde de
        // todos modos es improbable que no haya muros en cualquier nivel.
        outCoords.x = MathUtils.random(32, (int)gameScreen.getWorldWidth() - 32 - 64);
        outCoords.y = MathUtils.random(32, (int)gameScreen.getWorldHeight() - 32 - 64);

        Rectangle actorRect = game.rectPool.obtain();
        try {
            for (Actor currGroup : gameScreen.getWorldGroup().getChildren()) {
                if (currGroup instanceof Group) {
                    for (Actor actor : ((Group)currGroup).getChildren()) {
                        if (actor instanceof CollisionableActor) {
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
                                if (actor instanceof PlayerActor
                                        || !((CollisionableActor)actor).checkAllowOverlap(actorId, outCoords))
                                {
                                    return false;
                                } else {
                                    overlappedActors.add(actor);
                                }
                            }
                        }
                    }
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
