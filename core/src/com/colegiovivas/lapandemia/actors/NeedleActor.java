package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;

public class NeedleActor extends GenerableActor {
    private final Texture texture;
    private final LaPandemia game;

    public NeedleActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("needle.png");

        setTouchable(Touchable.enabled);
        setWidth(22);
        setHeight(64);
    }

    @Override
    public void reset() {
        setPosition(0, 0);
    }

    @Override
    public void drawNotBlinking(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void collidedBy(CollisionableActor actor, ActorId id, int srcX, int srcY) {
        if (id == ActorId.PLAYER) {
            remove();
        }
    }
}
