package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.pooling.PoolableArray;
import com.colegiovivas.lapandemia.pooling.PoolableRectangle;

public class CollisionInfo implements Pool.Poolable {
    private final LaPandemia game;

    // Actor cuya colisión con otros actores se estudia y Stage al que pertenece.
    public Actor collidingActor;
    public Stage stage;

    // Los diferentes tipos de actores con los que collidingActor entraría en colisión.
    public PoolableArray<Actor> walls;
    public PoolableArray<Actor> fans;
    public PoolableArray<Actor> viruses;
    public PlayerActor player;

    // Desplazamiento que collidingActor debe realizar finalmente. En caso de no haber
    // colisión, se corresponderá con su desplazamiento original. Si la colisión sí
    // ocurriría, estas variables reflejarán el necesario para mover a collidingActor lo
    // máximo posible sin llegar a entrar en colisión.
    public float effectiveXDisplacement;
    public float effectiveYDisplacement;

    public CollisionInfo(final LaPandemia game)
    {
        this.game = game;
    }

    @Override
    public void reset() {
        if (walls != null) {
            game.actorArrayPool.free(walls);
            walls = null;
        }
        if (fans != null) {
            game.actorArrayPool.free(fans);
            fans = null;
        }
        if (viruses != null) {
            game.actorArrayPool.free(viruses);
            viruses = null;
        }
        player = null;
        collidingActor = null;
        stage = null;
        effectiveXDisplacement = 0;
        effectiveYDisplacement = 0;
    }

    public CollisionInfo init(Actor collidingActor, float xDisplacement, float yDisplacement) {
        int xDir = xDisplacement == 0 ? 0 : xDisplacement < 0 ? -1 : 1;
        int yDir = yDisplacement == 0 ? 0 : yDisplacement < 0 ? -1 : 1;
        this.walls = game.actorArrayPool.obtain().init();
        this.fans = game.actorArrayPool.obtain().init();
        this.viruses = game.actorArrayPool.obtain().init();
        this.collidingActor = collidingActor;
        this.stage = collidingActor.getStage();
        effectiveXDisplacement = xDisplacement;
        effectiveYDisplacement = yDisplacement;

        // Rectángulo del PlayerActor tras moverse a la siguiente posición.
        PoolableRectangle rPlayer = game.rectPool.obtain().init();
        // Rectángulo de otro actor para comprobar si hay colisión con rPlayer.
        PoolableRectangle rOther = game.rectPool.obtain().init();
        PoolableArray<Actor> collidedActors = game.actorArrayPool.obtain().init();

        try {
            rPlayer.set(
                    collidingActor.getX() + xDisplacement,
                    collidingActor.getY() + yDisplacement,
                    collidingActor.getWidth(),
                    collidingActor.getHeight());
            for (Actor actor : stage.getActors()) {
                if (actor != collidingActor) {
                    rOther.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
                    if (rPlayer.overlaps(rOther)) {
                        float distance = getCollisionDistance(collidingActor, actor, xDir, yDir);
                        for (int i = collidedActors.size - 1; i >= 0; i--) {
                            Actor collidedActor = collidedActors.get(i);
                            if (getCollisionDistance(collidingActor, collidedActor, xDir, yDir) > distance) {
                                // `actor` está físicamente antepuesto a `collidedActors.get(i)`,
                                // por lo que este último no es un candidato real a ser colisionado.
                                collidedActors.removeIndex(i);
                            }
                        }
                        collidedActors.add(actor);
                    }
                }
            }

            // `collidingActor` chocará simultáneamente con todos los miembros de collidedActors.
            for (Actor actor : collidedActors) {
                float collisionDistance = getCollisionDistance(collidingActor, actor, xDir, yDir) - 1;
                effectiveXDisplacement = xDir*collisionDistance;
                effectiveYDisplacement = yDir*collisionDistance;
                if (actor instanceof PlayerActor) {
                    player = (PlayerActor)actor;
                }
                else if (actor instanceof WallActor) {
                    walls.add(actor);
                }
                else if (actor instanceof FanActor) {
                    fans.add(actor);
                }
                else if (actor instanceof VirusActor) {
                    viruses.add(actor);
                }
            }
        } finally {
            game.rectPool.free(rPlayer);
            game.rectPool.free(rOther);
            game.actorArrayPool.free(collidedActors);
        }

        return this;
    }

    // Obtener la cantidad mínima de píxeles que collidingActor debe recorrer para colisionar
    // con collidedActor, dada la dirección actual de collidingActor.
    private static float getCollisionDistance(Actor collidingActor, Actor collidedActor, int xDir, int yDir) {
        if (xDir == -1) return collidingActor.getX() - (collidedActor.getX() + collidedActor.getWidth() - 1);
        if (xDir == 1) return collidedActor.getX() - (collidingActor.getX() + collidingActor.getWidth() - 1);
        if (yDir == -1) return collidingActor.getY() - (collidedActor.getY() + collidedActor.getHeight() - 1);
        else return collidedActor.getY() - (collidingActor.getY() + collidingActor.getHeight() - 1);
    }
}
