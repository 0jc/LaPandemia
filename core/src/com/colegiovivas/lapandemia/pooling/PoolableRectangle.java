package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class PoolableRectangle extends Rectangle implements Pool.Poolable {
    public PoolableRectangle init() {
        return this;
    }

    @Override
    public void reset() {
        set(0, 0, 0, 0);
    }
}
