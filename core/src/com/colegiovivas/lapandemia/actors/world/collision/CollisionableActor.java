package com.colegiovivas.lapandemia.actors.world.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.actors.world.ActorId;

public abstract class CollisionableActor extends Actor {
    protected CollisionDispatcher collisionDispatcher;

    public void setCollisionDispatcher(CollisionDispatcher collisionDispatcher) {
        this.collisionDispatcher = collisionDispatcher;
    }

    public void collidedBy(CollisionableActor actor) {

    }
    public void collidedWith(CollisionableActor actor) {

    }

    public abstract ActorId getActorId();

    public boolean checkAllowOverlap(ActorId id, Rectangle initPos) {
        return false;
    }
}
