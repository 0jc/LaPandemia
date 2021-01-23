package com.example.testgame.pooling;

import com.badlogic.gdx.utils.Pool;

public class RectanglePool extends Pool<PoolableRectangle> {
    @Override
    protected PoolableRectangle newObject() {
        return new PoolableRectangle();
    }
}
