package com.colegiovivas.lapandemia.actors.world.generator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

/**
 * Actor generable por un generador de actores.
 */
public abstract class GenerableActor extends CollisionableActor implements Pool.Poolable {
    /**
     * Generador de donde proviene el actor.
     */
    private ActorGenerator generator;

    /**
     * Tiempo de vida en segundos. Si no es nulo, el actor se autoelimina una vez transcurrido
     * el tiempo.
     */
    private Float ttl;

    /**
     * Tiempo transcurrido desde la creación del actor.
     */
    private float age;

    /**
     * Segundos durante los que el actor parpadeará antes de desaparecer finalmente por el TTL.
     */
    private static final float BLINK_PERIOD = 5;

    /**
     * Frecuencia de parpadeo.
     */
    private static final float BLINK_TICK = 0.25f;

    /**
     * Inicia el actor para ser válido de utilizar incluso si fue reciclado a través de un fondo.
     * @return La propia instancia del actor, para facilitar la concatenación de llamadas.
     */
    public GenerableActor init() {
        age = 0;
        return this;
    }

    /**
     * @param generator El generador de donde proviene el actor.
     */
    public void setGenerator(ActorGenerator generator) {
        this.generator = generator;
    }

    /**
     * @param ttl El tiempo de vida del actor.
     */
    public void setTtl(Float ttl) {
        this.ttl = ttl;
    }

    /**
     * Actualiza el estado del actor, posiblemente eliminándolo por haber caducado.
     * Si el tiempo de vida no ha finalizado, tan solo se delega en actWithinTTL().
     * @param delta Tiempo en segundos transcurrido desde la última actualización.
     */
    @Override
    public final void act(float delta) {
        age += delta;
        if (ttl != null && age >= ttl) {
            remove();
        } else {
            actWithinTTL(delta);
        }
    }

    /**
     * Actualiza el estado del actor, partiendo de la base de que su tiempo de vida aún
     * no ha finalizado.
     * @param delta Tiempo en segundos transcurrido desde la última actualización.
     */
    public void actWithinTTL(float delta) {
    }

    /**
     * Dibuja el actor en pantalla, excepto en caso de estar en la fase invisible del parpadeo
     * final por la que los actores temporales pasan antes de desaparecer. En caso de no estar
     * en esa fase, simplemente se delega en drawNotBlinking().
     * @param batch Batch de Libgdx con el que se dibuja.
     * @param parentAlpha Valor alpha de Libgdx del elemento padre del batch.
     */
    @Override
    public final void draw(Batch batch, float parentAlpha) {
        if (ttl == null || ttl - age > BLINK_PERIOD || Math.ceil((ttl - age)/BLINK_TICK) % 2 == 1) {
            drawNotBlinking(batch, parentAlpha);
        }
    }

    /**
     * Dibuja el actor en pantalla, partiendo de la base de que no está en la fase invisible
     * del parpadeo final.
     * @param batch Batch de Libgdx con el que se dibuja.
     * @param parentAlpha Valor alpha de Libgdx del elemento padre del batch.
     */
    public void drawNotBlinking(Batch batch, float parentAlpha) {
    }

    @Override
    public boolean remove() {
        if (generator != null) generator.remove(this);
        return super.remove();
    }
}
