package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;
import com.colegiovivas.lapandemia.actors.world.generator.GenerableActor;

/**
 * Actor mascarilla.
 */
public class MaskActor extends GenerableActor {
    /**
     * Textura del actor.
     */
    private final TextureAtlas.AtlasRegion texture;

    private final LaPandemia game;

    public MaskActor(final LaPandemia game) {
        this.game = game;
        this.texture = ((TextureAtlas)game.assetManager.get("images.pack")).findRegion("mask");

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
        return ActorId.MASK;
    }

    @Override
    public boolean checkAllowOverlap(ActorId id, Rectangle initPos) {
        switch (id) {
            case NEEDLE:
            case PAPER:
            case MASK:
            case VIRUS:
                return true;
        }

        return false;
    }
}
