package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PoolableArray<T> extends Array<T> implements Pool.Poolable {
    public PoolableArray<T> init() {
        return this;
    }

    @Override
    public void reset() {
        clear();
    }
}
