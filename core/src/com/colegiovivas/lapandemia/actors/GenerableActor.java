package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;
import com.colegiovivas.lapandemia.actors.generator.ActorGenerator;

public abstract class GenerableActor extends CollisionableActor implements Pool.Poolable {
    private ActorGenerator generator;

    public GenerableActor init() {
        return this;
    }

    public void setGenerator(ActorGenerator generator) {
        this.generator = generator;
    }

    @Override
    public boolean remove() {
        generator.remove(this);
        return super.remove();
    }
}
