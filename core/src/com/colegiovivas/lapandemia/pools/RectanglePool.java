package com.colegiovivas.lapandemia.pools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class RectanglePool extends Pool<Rectangle> {
    private static abstract class PoolableRectangle extends Rectangle implements Pool.Poolable {}

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
