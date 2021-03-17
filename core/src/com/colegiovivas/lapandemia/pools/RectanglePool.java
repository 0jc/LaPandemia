package com.colegiovivas.lapandemia.pools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * Fondo para instancias de Rectangle.
 */
public class RectanglePool extends Pool<Rectangle> {
    /**
     * Rectangle que implementa Poolable.
     */
    private static abstract class PoolableRectangle extends Rectangle implements Pool.Poolable {}

    /**
     * Inicializa el fondo.
     * @param initialCapacity Capacidad inicial del fondo.
     * @param max Número máximo de instancias en el fondo.
     */
    public RectanglePool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    protected PoolableRectangle newObject() {
        return new PoolableRectangle() {
            @Override
            public void reset() {
                set(0, 0, 0, 0);
            }
        };
    }
}
