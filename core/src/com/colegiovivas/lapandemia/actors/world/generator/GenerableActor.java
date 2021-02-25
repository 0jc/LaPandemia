package com.colegiovivas.lapandemia.actors.world.generator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

public abstract class GenerableActor extends CollisionableActor implements Pool.Poolable {
    private ActorGenerator generator;

    // Tiempo de vida en segundos. Si no es nulo, el actor se autoelimina una vez transcurrido.
    private Float ttl;
    private float age;

    // Segundos durante los que parpadeará antes de desaparecer finalmente por el TTL.
    private static final float BLINK_PERIOD = 5;
    // Frecuencia de parpadeo.
    private static final float BLINK_TICK = 0.25f;

    public GenerableActor init() {
        age = 0;
        return this;
    }

    public void setGenerator(ActorGenerator generator) {
        this.generator = generator;
    }

    public void setTtl(Float ttl) {
        this.ttl = ttl;
    }

    @Override
    public final void act(float delta) {
        age += delta;
        if (ttl != null && age >= ttl) {
            remove();
        } else {
            actWithinTTL(delta);
        }
    }

    public void actWithinTTL(float delta) {
    }

    @Override
    public final void draw(Batch batch, float parentAlpha) {
        if (ttl == null || ttl - age > BLINK_PERIOD || Math.ceil((ttl - age)/BLINK_TICK) % 2 == 1) {
            drawNotBlinking(batch, parentAlpha);
        }
    }

    public void drawNotBlinking(Batch batch, float parentAlpha) {
    }

    @Override
    public boolean remove() {
        if (generator != null) generator.remove(this);
        return super.remove();
    }
}
