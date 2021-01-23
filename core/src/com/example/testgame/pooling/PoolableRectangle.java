package com.example.testgame.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class PoolableRectangle extends Rectangle implements Pool.Poolable {
    @Override
    public void reset() {
        set(0, 0, 0, 0);
    }
}
