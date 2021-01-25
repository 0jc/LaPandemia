package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.CollisionInfo;

public class CollisionInfoPool extends Pool<CollisionInfo> {
    private final LaPandemia game;

    public CollisionInfoPool(final LaPandemia game) {
        this.game = game;
    }

    @Override
    protected CollisionInfo newObject() {
        return new CollisionInfo(game);
    }
}
