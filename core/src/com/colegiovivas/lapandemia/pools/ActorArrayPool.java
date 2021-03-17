package com.colegiovivas.lapandemia.pools;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Fondo para instancias de Array&lt;Actor&gt;.
 */
public class ActorArrayPool extends Pool<Array<Actor>> {
    /**
     * Array que implementa Poolable.
     */
    private static abstract class PoolableArray<T> extends Array<T> implements Pool.Poolable {}

    @Override
    protected PoolableArray<Actor> newObject() {
        return new PoolableArray<Actor>() {
            @Override
            public void reset() {
                clear();
            }
        };
    }
}
