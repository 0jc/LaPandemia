package com.colegiovivas.lapandemia.actors.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.*;

public class CollisionDispatcher {
    private final LaPandemia game;
    private final Stage stage;
    private ArrayMap<Class<? extends CollisionableActor>, ActorId> actorRegistry;

    public CollisionDispatcher(LaPandemia game, Stage stage) {
        this.game = game;
        this.stage = stage;
        actorRegistry = new ArrayMap<>();
    }

    public void register(ActorId id, Class<? extends CollisionableActor> actorClass) {
        actorRegistry.put(actorClass, id);
    }

    public void tryMove(CollisionableActor actor, float xDisplacement, float yDisplacement) {
        float srcX = actor.getX();
        float srcY = actor.getY();
        int xDir = xDisplacement == 0 ? 0 : xDisplacement < 0 ? -1 : 1;
        int yDir = yDisplacement == 0 ? 0 : yDisplacement < 0 ? -1 : 1;

        // Rectángulo del PlayerActor tras moverse a la siguiente posición.
        Rectangle rActor = game.rectPool.obtain();
        // Rectángulo de otro actor para comprobar si hay colisión con rActor.
        Rectangle rOther = game.rectPool.obtain();

        Array<Actor> collidedActors = game.actorArrayPool.obtain();
        Float minCollisionDistance = null;
        try {
            rActor.set(actor.getX() + xDisplacement, actor.getY() + yDisplacement, actor.getWidth(),
                    actor.getHeight());
            for (Actor currActor : stage.getActors()) {
                if (currActor != actor) {
                    rOther.set(currActor.getX(), currActor.getY(), currActor.getWidth(), currActor.getHeight());
                    if (rActor.overlaps(rOther)) {
                        float collisionDistance = collisionDistance(actor, currActor, xDir, yDir);
                        if (minCollisionDistance == null || minCollisionDistance >= collisionDistance) {
                            if (minCollisionDistance == null || minCollisionDistance > collisionDistance) {
                                minCollisionDistance = collisionDistance;
                                collidedActors.clear();
                            }
                            collidedActors.add(currActor);
                        }
                    }
                }
            }

            float effectiveDx = minCollisionDistance == null ? xDisplacement : xDir*minCollisionDistance;
            float effectiveDy = minCollisionDistance == null ? yDisplacement : yDir*minCollisionDistance;
            actor.setPosition(srcX + effectiveDx, srcY + effectiveDy);

            // `collidingActor` chocará **simultáneamente** con todos los miembros de collidedActors.
            // Es decir, no se da la situación de que algunos de estos actores estén físicamente
            // antepuestos a otros.
            ActorId collidingActorId = actorRegistry.get(actor.getClass());
            for (Actor collidedActor : collidedActors) {
                ActorId collidedActorId = actorRegistry.get(((CollisionableActor)collidedActor).getClass());
                actor.collidedWith((CollisionableActor)collidedActor, collidedActorId, srcX, srcY);
                ((CollisionableActor)collidedActor).collidedBy(actor, collidingActorId, srcX, srcY);
            }
        } finally {
            game.rectPool.free(rActor);
            game.rectPool.free(rOther);
            game.actorArrayPool.free(collidedActors);
        }
    }

    // Obtener la cantidad mínima de píxeles que collidingActor debe recorrer para colisionar
    // con collidedActor, dada la dirección actual de collidingActor. Tras realizarse el
    // desplazamiento, los actores se mostrarían el uno justo al lado del otro sin superponerse.
    private static float collisionDistance(
            Actor collidingActor, Actor collidedActor, int xDir, int yDir)
    {
        if (xDir != 0 && yDir != 0) {
            boolean collidingOnY = collidingActor.getY() < collidedActor.getY() + collidedActor.getHeight()
                                && collidingActor.getY() + collidingActor.getHeight() > collidedActor.getY();
            boolean collidingOnX = collidingActor.getX() < collidedActor.getX() + collidedActor.getWidth()
                                && collidingActor.getX() + collidingActor.getWidth() > collidedActor.getX();

            if (collidingOnX && !collidingOnY) {
               return collisionDistance(collidingActor, collidedActor, 0, yDir);
            } else if (!collidingOnX && collidingOnY) {
                return collisionDistance(collidingActor, collidedActor, xDir, 0);
            } else {
                return Math.max(
                        collisionDistance(collidingActor, collidedActor, xDir, 0),
                        collisionDistance(collidingActor, collidedActor, 0, yDir));
            }
        } else if (xDir == -1) {
            return Math.abs(collidingActor.getX() - (collidedActor.getX() + collidedActor.getWidth()));
        } else if (xDir == 1) {
            return Math.abs(collidedActor.getX() - (collidingActor.getX() + collidingActor.getWidth()));
        } else if (yDir == -1) {
            return Math.abs(collidingActor.getY() - (collidedActor.getY() + collidedActor.getHeight()));
        } else {
            return Math.abs(collidedActor.getY() - (collidingActor.getY() + collidingActor.getHeight()));
        }
    }
}
