package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;
import com.colegiovivas.lapandemia.actors.world.generator.GenerableActor;

/**
 * Actor virus.
 */
public class VirusActor extends GenerableActor {
    /**
     * Velocidad a la que se desplaza el virus.
     */
    private static final float SPEED = 100;

    /**
     * Tiempo mínimo que el virus espera antes de cambiar de dirección.
     */
    private static final float MIN_DIR_TICK = 0.7f;

    /**
     * Tiempo máximo que el virus espera antes de cambiar de dirección.
     */
    private static final float MAX_DIR_TICK = 1.5f;

    private final LaPandemia game;

    /**
     * Animación que realiza el virus.
     */
    private final Animation<TextureRegion> animation;

    /**
     * Tiempo total que se lleva reproduciendo la animación.
     */
    private float animationTime;

    /**
     * True si el virus está vivo o false si no lo está.
     */
    private boolean alive;

    /**
     * Tiempo que el virus lleva moviéndose en la dirección actual.
     */
    private float directionTime;

    /**
     * Tiempo total durante el que el virus debe desplazarse en la dirección actual.
     */
    private float directionTick;

    /**
     * Dirección en el eje X del virus (-1 = izquierda, 1 = derecha, 0 = ninguna).
     */
    private int xDir;

    /**
     * Dirección en el eje Y del virus (-1 = abajo, 1 = arriba, 0 = ninguna).
     */
    private int yDir;

    // El virus tiene una velocidad, pero debe moverse una cantidad entera de píxeles
    // por frame. Por tanto, si, por ejemplo, debe moverse 3.4 píxeles hacia abajo,
    // a su posición y se le sumará -3 y untraveledY será igual a -0.4. Este -0.4
    // se acumulará así para el siguiente frame, por lo que, si en este debe bajar otros
    // 2.8 píxeles, la distancia total que deberá recorrer será -0.4 + (-2.8) = -3.2,
    // bajando otros 3 píxeles y teniendo untraveledY==-0.2 para el siguiente frame,
    // y así sucesivamente.
    /**
     * Fracción de píxel acumulada de movimiento en el eje X.
     */
    private float untraveledX;

    /**
     * Fracción de píxel acumulada de movimiento en el eje Y.
     */
    private float untraveledY;

    public VirusActor(final LaPandemia game) {
        this.game = game;

        animation = new Animation<TextureRegion>(0.2f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("virus"));

        setTouchable(Touchable.enabled);
        untraveledX = 0;
        untraveledY = 0;
    }

    @Override
    public VirusActor init() {
        super.init();
        alive = true;
        return this;
    }

    @Override
    public void reset() {
        animationTime = 0;
        directionTime = 0;
        directionTick = 0;
        untraveledX = 0;
        untraveledY = 0;
        xDir = 0;
        yDir = 0;
        alive = false;
    }

    @Override
    public boolean remove() {
        alive = false;
        return super.remove();
    }

    @Override
    public void drawNotBlinking(Batch batch, float parentAlpha) {
        if (alive) {
            animationTime += getWorld().isPaused() ? 0 : Gdx.graphics.getDeltaTime();
            batch.draw(
                    animation.getKeyFrame(animationTime, true),
                    getX(), getY());
        }
    }

    @Override
    public void actWithinTTL(float delta) {
        if (!alive) {
            return;
        }

        directionTime += delta;
        if (directionTick == 0 || directionTime >= directionTick) {
            directionTick = MathUtils.random(MIN_DIR_TICK, MAX_DIR_TICK);
            directionTime = 0;
            xDir = MathUtils.random(-1, 1);
            yDir = MathUtils.random(-1 ,1);
            untraveledX = 0;
            untraveledY = 0;
        }

        untraveledX += xDir * SPEED * delta;
        untraveledY += yDir * SPEED * delta;
        int xDisplacement = xDir == 1 ? (int)Math.floor(untraveledX) : (int)Math.ceil(untraveledX);
        int yDisplacement = yDir == 1 ? (int)Math.floor(untraveledY) : (int)Math.ceil(untraveledY);
        untraveledX -= xDisplacement;
        untraveledY -= yDisplacement;

        collisionDispatcher.tryMove(this, xDisplacement, yDisplacement);
    }

    @Override
    public void collidedWith(CollisionableActor actor) {
        switch (actor.getActorId()) {
            case WALL:
                directionTick = 0;
                break;

            case FAN:
                remove();
                break;

            case PLAYER:
                caughtPlayer();
                break;
        }
    }

    @Override
    public ActorId getActorId() {
        return ActorId.VIRUS;
    }

    @Override
    public void collidedBy(CollisionableActor actor) {
        if (actor.getActorId() == ActorId.PLAYER) {
            caughtPlayer();
        }
    }

    /**
     * Evento que se lanza cuando el virus colisiona con el jugador.
     */
    private void caughtPlayer() {
        remove();
    }

    @Override
    public boolean checkAllowOverlap(ActorId id, Rectangle initPos) {
        switch (id) {
            case NEEDLE:
            case MASK:
            case PAPER:
                return true;
        }

        return false;
    }
}
