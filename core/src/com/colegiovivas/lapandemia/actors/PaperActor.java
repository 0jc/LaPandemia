package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;

public class PaperActor extends GenerableActor {
    private final Texture texture;
    private final LaPandemia game;

    public PaperActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("toiletpaper.png");

        setTouchable(Touchable.enabled);
        setSize(48, 48);
    }

    @Override
    public void reset() {
        setPosition(0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void collidedBy(CollisionableActor actor, ActorId id, int srcX, int srcY) {
        if (id == ActorId.PLAYER) {
            remove();
        }
    }
}
