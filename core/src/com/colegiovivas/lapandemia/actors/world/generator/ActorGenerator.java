package com.colegiovivas.lapandemia.actors.world.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;
import com.colegiovivas.lapandemia.pools.ActorArrayPool;
import com.colegiovivas.lapandemia.pools.RectanglePool;
import com.colegiovivas.lapandemia.screens.game.World;

/**
 * Generador periódico de actores de mundo colisionables.
 */
public class ActorGenerator {
    /**
     * Distancia mínima que los actores generados deben guardar con el personaje principal
     * en el momento de su aparición.
     */
    private static final float SAFE_DISTANCE = 400;

    /**
     * Cada cuánto se genera un nuevo actor.
     */
    private final float tick;

    /**
     * Anchura de los actores generados.
     */
    private final float width;

    /**
     * Altura de los actores generados.
     */
    private final float height;

    /**
     * Número máximo de actores de este tipo que pueden existir simultáneamente.
     */
    private final int maxCount;

    /**
     * Fondo de donde se obtienen los actores.
     */
    private final Pool<GenerableActor> generableActorPool;

    /**
     * Tiempo de vida en segundos. Null indica un tiempo de vida ilimitado.
     */
    private final Float ttl;

    /**
     * ID de los actores generados.
     */
    private final ActorId actorId;

    /**
     * Grupo al que se añaden los actores generados.
     */
    private final Group destGroup;

    /**
     * Fondo de instancias de Array&lt;Actor&gt;.
     */
    private final ActorArrayPool actorArrayPool;

    /**
     * Fondo de instancias de Rectangle.
     */
    private final RectanglePool rectPool;

    /**
     * Controlador del mundo al que pertenecen los actores generados.
     */
    private final World world;

    /**
     * Cantidad actual de actores generados.
     */
    private int count;

    /**
     * Tiempo transcurrido desde la generación del último actor.
     */
    private float lastActorTime;

    /**
     * Inicializa el generador.
     * @param generableActorClass Clase de los actores generados. DEBE implementar un constructor de un único
     *                            argumento de tipo AssetManager, que será el que será llamado para inicializar
     *                            nuevas instancias.
     * @param actorId ID del tipo de actor.
     * @param destGroup Subgrupo del grupo raíz del mundo al que serán añadidos los actores.
     * @param width Anchura de los actores.
     * @param height Altura de los actores.
     * @param tick Tiempo de espera en segundos entre la generación de actores.
     * @param maxCount Número máximo de actores que podrán coexistir.
     * @param ttl Tiempo de vida en segundos de los actores.
     * @param assetManager Gestor de recursos utilizado por la aplicación.
     * @param world Mundo en el que existirán los actores.
     */
    public ActorGenerator(final Class<? extends GenerableActor> generableActorClass, ActorId actorId,
                          Group destGroup, float width, float height, float tick, int maxCount, Float ttl,
                          final AssetManager assetManager, World world)
    {
        this.actorId = actorId;
        this.destGroup = destGroup;
        this.tick = tick;
        this.width = width;
        this.height = height;
        this.maxCount = maxCount;
        this.ttl = ttl;
        this.world = world;

        actorArrayPool = new ActorArrayPool();
        rectPool = new RectanglePool(2, 2);
        this.generableActorPool = new Pool<GenerableActor>(maxCount, maxCount) {
            @Override
            protected GenerableActor newObject() {
                try {
                    return generableActorClass.getDeclaredConstructor(AssetManager.class).newInstance(assetManager);
                } catch (Exception e) {
                    Gdx.app.error("LaPandemia", "ActorGenerator: " + e.getMessage());
                }

                return null;
            }
        };
    }

    /**
     * @return Fondo de donde se obtienen los actores.
     */
    public Pool<GenerableActor> getPool() {
        return generableActorPool;
    }

    /**
     * Notifica al generador de que un actor ha sido eliminado del mundo, para que pueda ser
     * liberado y se actualice el contador de actores.
     * @param actor Actor eliminado.
     */
    public void remove(GenerableActor actor) {
        count--;
        generableActorPool.free(actor);
    }

    /**
     * Actualiza el estado del generador.
     * @param delta Tiempo en segundos transcurrido desde la última actualización.
     */
    public void render(float delta) {
        if (count == maxCount) {
            lastActorTime = 0;
        } else {
            lastActorTime += delta;
            if (lastActorTime >= tick) {
                Rectangle rect = rectPool.obtain();
                Array<Actor> overlappedActors = actorArrayPool.obtain();
                rect.set(0, 0, width, height);
                try {
                    if (tryAssignCoords(rect, overlappedActors)) {
                        lastActorTime = 0;
                        GenerableActor actor = generableActorPool.obtain().init();
                        actor.setGenerator(this);
                        actor.setWorld(world);
                        actor.setBounds(rect.x, rect.y, rect.width, rect.height);
                        actor.setCollisionDispatcher(world.getCollisionDispatcher());
                        actor.setTtl(ttl);
                        destGroup.addActor(actor);
                        count++;

                        for (Actor overlappedActor : overlappedActors) {
                            ((CollisionableActor)overlappedActor).collidedBy(actor);
                        }
                    }
                } finally {
                    rectPool.free(rect);
                    actorArrayPool.free(overlappedActors);
                }
            }
        }
    }

    /**
     * Reporta información sobre una hipotética posición inicial para un nuevo actor.
     * @param outCoords Coordenas que se desean para el actor.
     * @param overlappedActors Lista en la que se guardarán los actores con los que se
     *                         colisionará por la aparición del actor.
     * @return True si y solo si las coordenadas solicitadas son aceptables.
     */
    private boolean tryAssignCoords(Rectangle outCoords, Array<Actor> overlappedActors) {
        // No generamos coordenadas demasiado cerca de los bordes del mapa, donde de
        // todos modos es improbable que no haya muros en cualquier nivel.
        outCoords.x = MathUtils.random(32, (int)world.getWidth() - 32 - 64);
        outCoords.y = MathUtils.random(32, (int)world.getHeight() - 32 - 64);

        Rectangle actorRect = rectPool.obtain();
        try {
            for (Actor currGroup : world.getRootGroup().getChildren()) {
                if (currGroup instanceof Group) {
                    for (Actor actor : ((Group)currGroup).getChildren()) {
                        if (actor instanceof CollisionableActor) {
                            if (actor instanceof PlayerActor) {
                                actorRect.x = actor.getX() - SAFE_DISTANCE;
                                actorRect.y = actor.getY() - SAFE_DISTANCE;
                                actorRect.width = 2 * SAFE_DISTANCE + actor.getWidth();
                                actorRect.height = 2 * SAFE_DISTANCE + actor.getHeight();
                            } else {
                                actorRect.x = actor.getX();
                                actorRect.y = actor.getY();
                                actorRect.width = actor.getWidth();
                                actorRect.height = actor.getHeight();
                            }

                            if (actorRect.overlaps(outCoords)) {
                                if (actor instanceof PlayerActor
                                        || !((CollisionableActor)actor).checkAllowOverlap(actorId, outCoords))
                                {
                                    return false;
                                } else {
                                    overlappedActors.add(actor);
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            rectPool.free(actorRect);
        }
        return true;
    }

    /**
     * Libera los recursos utilizados por el generador.
     */
    public void dispose() {
        generableActorPool.clear();
        actorArrayPool.clear();
        rectPool.clear();
    }
}
