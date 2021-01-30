package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;
import com.colegiovivas.lapandemia.actors.generator.ActorGenerator;

public abstract class GenerableActor extends CollisionableActor implements Pool.Poolable {
    private ActorGenerator generator;

    public GenerableActor init(ActorGenerator generator, float x, float y) {
        this.generator = generator;
        setPosition(x, y);
        return this;
    }

    @Override
    public boolean remove() {
        generator.remove(this);
        return super.remove();
    }
}
