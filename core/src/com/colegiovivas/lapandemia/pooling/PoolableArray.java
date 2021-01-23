package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PoolableArray<T> extends Array<T> implements Pool.Poolable {
    @Override
    public void reset() {
        clear();
    }
}
