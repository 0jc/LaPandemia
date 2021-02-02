package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

public class PaperActor extends GenerableActor {
    private final Texture texture;
    private final LaPandemia game;

    public PaperActor(final LaPandemia game) {
        this.game = game;
        this.texture = game.assetManager.get("toiletpaper.png");

        setTouchable(Touchable.enabled);
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
    public void collidedBy(CollisionableActor actor) {
        if (actor.getActorId() == ActorId.PLAYER) {
            remove();
        }
    }

    @Override
    public ActorId getActorId() {
        return ActorId.PAPER;
    }

    @Override
    public boolean checkAllowOverlap(ActorId id, Rectangle initPos) {
        switch (id) {
            case NEEDLE:
            case MASK:
            case PAPER:
            case VIRUS:
                return true;
        }

        return false;
    }
}
