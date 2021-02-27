package com.colegiovivas.lapandemia.actors.world.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.actors.WorldActor;
import com.colegiovivas.lapandemia.actors.world.ActorId;

/**
 * Actor procesable mediante el gestor de colisiones (CollisionDispatcher).
 */
public abstract class CollisionableActor extends WorldActor {
    /**
     * Gestor de colisiones al que el actor reporta cambios de posición.
     */
    protected CollisionDispatcher collisionDispatcher;

    /**
     * @param collisionDispatcher Gestor de colisiones al que el actor reporta cambios de posición.
     */
    public void setCollisionDispatcher(CollisionDispatcher collisionDispatcher) {
        this.collisionDispatcher = collisionDispatcher;
    }

    /**
     * Evento lanzado cuando el actor es colisionado pasivamente por otro CollisionableActor.
     * @param actor Actor que ha colisionado.
     */
    public void collidedBy(CollisionableActor actor) {

    }

    /**
     * Evento lanzado cuando el actor colisiona activamente con otro CollisionableActor.
     * @param actor Actor con el que se ha colisionado.
     */
    public void collidedWith(CollisionableActor actor) {

    }

    /**
     * @return ID del tipo de actor.
     */
    public abstract ActorId getActorId();

    /**
     * Informa a otro actor de si puede entrar en colisión con este.
     * @param id ID del actor que intenta entrar en colisión.
     * @param initPos Posición que el actor adoptaría para comenzar la colisión.
     * @return True si y solo si el actor puede colisionar.
     */
    public boolean checkAllowOverlap(ActorId id, Rectangle initPos) {
        return false;
    }
}
