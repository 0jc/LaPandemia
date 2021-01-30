package com.colegiovivas.lapandemia.actors.collision;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.actors.ActorId;

public class CollisionableActor extends Actor {
    protected CollisionDispatcher collisionDispatcher;

    public void setCollisionDispatcher(CollisionDispatcher collisionDispatcher) {
        this.collisionDispatcher = collisionDispatcher;
    }

    public void collidedBy(CollisionableActor actor, ActorId id, int srcX, int srcY) {

    }
    public void collidedWith(CollisionableActor actor, ActorId id, int srcX, int srcY) {

    }
}
