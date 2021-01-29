package com.colegiovivas.lapandemia.gameplay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.gameplay.generator.ActorGenerator;

public abstract class GenerableActor extends Actor implements Pool.Poolable {
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
