package com.colegiovivas.lapandemia.actors.world.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.colegiovivas.lapandemia.LaPandemia;

/**
 * Gestor de colisiones entre actores de un mismo mundo.
 */
public class CollisionDispatcher {
    private final LaPandemia game;

    /**
     * Grupo compuesto por subgrupos donde se buscan actores colisionables.
     */
    private final Group worldGroup;

    public CollisionDispatcher(LaPandemia game, Group worldGroup) {
        this.game = game;
        this.worldGroup = worldGroup;
    }

    /**
     * Informa al gestor de colisiones de un desplazamiento que un actor quiere realizar.
     * Un actor nunca quedará superpuesto a otro que no lo permita, de modo que su trayectoria
     * puede acortarse parcialmente, pero aquellos actores que no permitan la superposición
     * serán notificados igualmente de la colisión. Aquellos actores que sí permitan la
     * superposición serán notificados de la colisión en el momento que se produzca, pero no
     * en los frames consecuentes. El movimiento final no lo realiza el propio actor sino
     * el propio gestor de colisiones.
     * @param actor Actor que intenta desplazarse.
     * @param xDisplacement Diferencia de posición sobre el eje X.
     * @param yDisplacement Diferencia de posición sobre el eje Y.
     */
    public void tryMove(CollisionableActor actor, int xDisplacement, int yDisplacement) {
        int srcX = (int)actor.getX();
        int srcY = (int)actor.getY();
        int xDir = xDisplacement == 0 ? 0 : xDisplacement < 0 ? -1 : 1;
        int yDir = yDisplacement == 0 ? 0 : yDisplacement < 0 ? -1 : 1;

        // Rectángulo del PlayerActor tras moverse a la siguiente posición.
        Rectangle rBefore = game.rectPool.obtain();
        // Rectángulo del PlayerActor tras moverse a la siguiente posición.
        Rectangle rAfter = game.rectPool.obtain();
        // Rectángulo de otro actor para comprobar si hay colisión con rAfter.
        Rectangle rOther = game.rectPool.obtain();

        Array<Actor> collidedActors = game.actorArrayPool.obtain();
        Integer minBumpDistance = null;
        try {
            rBefore.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            rAfter.set(actor.getX() + xDisplacement, actor.getY() + yDisplacement, actor.getWidth(),
                    actor.getHeight());
            for (Actor currGroup : worldGroup.getChildren()) {
                if ( !(currGroup instanceof Group) ) {
                    continue;
                }

                for (Actor currActor : ((Group)currGroup).getChildren()) {
                    if ( !(currActor instanceof CollisionableActor && currActor != actor) ) {
                        continue;
                    }

                    rOther.set(currActor.getX(), currActor.getY(), currActor.getWidth(), currActor.getHeight());
                    if (rAfter.overlaps(rOther) && !rBefore.overlaps(rOther)) {
                        if (((CollisionableActor)currActor).checkAllowOverlap(actor.getActorId(), rAfter)) {
                            continue;
                        }

                        int collisionDistance = collisionDistance(actor, currActor, xDir, yDir);
                        if (minBumpDistance == null || minBumpDistance >= collisionDistance) {
                            if (minBumpDistance == null || minBumpDistance > collisionDistance) {
                                minBumpDistance = collisionDistance;
                                collidedActors.clear();
                            }
                            collidedActors.add(currActor);
                        }
                    }
                }
            }

            // `collidingActor` chocará **simultáneamente** con todos los miembros actuales de
            // `collidedActors`, sin llegar a superponerse sobre ellos.
            int effectiveDx = minBumpDistance == null ? xDisplacement : xDir*minBumpDistance;
            int effectiveDy = minBumpDistance == null ? yDisplacement : yDir*minBumpDistance;
            actor.setPosition(srcX + effectiveDx, srcY + effectiveDy);

            // Ahora añadiremos también aquellos actores sobre los que nos estaremos superponiendo
            // al haber realizado este movimiento. Se trata también de colisiones, pero no producen
            // "efecto choque". Un ejemplo puede ser un powerup siendo `actor` un virus, o simplemente
            // actores que formen parte del decorado. Todos estos actores son traspasables.
            rAfter.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
            for (Actor currGroup : worldGroup.getChildren()) {
                if ( !(currGroup instanceof Group) ) {
                    continue;
                }

                for (Actor currActor : ((Group)currGroup).getChildren()) {
                    if ( !(currActor instanceof CollisionableActor && currActor != actor) ) {
                        continue;
                    }

                    rOther.set(currActor.getX(), currActor.getY(), currActor.getWidth(), currActor.getHeight());
                    if (rAfter.overlaps(rOther) && !rBefore.overlaps(rOther)) {
                        collidedActors.add(currActor);
                    }
                }
            }

            for (Actor collidedActor : collidedActors) {
                actor.collidedWith((CollisionableActor)collidedActor);
                ((CollisionableActor)collidedActor).collidedBy(actor);
            }
        } finally {
            game.rectPool.free(rBefore);
            game.rectPool.free(rAfter);
            game.rectPool.free(rOther);
            game.actorArrayPool.free(collidedActors);
        }
    }

    // Obtener la cantidad mínima de píxeles que collidingActor debe recorrer para colisionar
    // con collidedActor, dada la dirección actual de collidingActor. Tras realizarse el
    // desplazamiento, los actores se mostrarían el uno justo al lado del otro sin superponerse.

    /**
     * Devuelve la cantidad mínima de píxeles que collidingActor debe recorrer para colisionar
     * con collidedActor, dada la dirección actual de collidingActor. Tras realizarse el
     * desplazamiento, los actores se mostrarían el uno justo al lado del otro sin superponerse.
     * @param collidingActor Actor que se desplaza y colisiona activamente.
     * @param collidedActor Actor que es colisionado pasivamente.
     * @param xDir Dirección del desplazamiento sobre el eje X (1, 0 o -1).
     * @param yDir Dirección del desplazamiento sobre el eje Y (1, 0 o -1).
     * @return Cantidad de píxeles de desplazamiento para que collidingActor roce a collidedActor
     * sin llegar a superponerse.
     */
    private static int collisionDistance(
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
            return (int)Math.abs(collidingActor.getX() - (collidedActor.getX() + collidedActor.getWidth()));
        } else if (xDir == 1) {
            return (int)Math.abs(collidedActor.getX() - (collidingActor.getX() + collidingActor.getWidth()));
        } else if (yDir == -1) {
            return (int)Math.abs(collidingActor.getY() - (collidedActor.getY() + collidedActor.getHeight()));
        } else {
            return (int)Math.abs(collidedActor.getY() - (collidingActor.getY() + collidingActor.getHeight()));
        }
    }
}
