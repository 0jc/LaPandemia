package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.utils.Pool;

public class ArrayPool<T> extends Pool<PoolableArray<T>> {
    @Override
    protected PoolableArray<T> newObject() {
        return new PoolableArray<T>();
    }
}
