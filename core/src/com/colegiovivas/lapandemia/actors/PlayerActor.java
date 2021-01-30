package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.collision.CollisionableActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class PlayerActor extends CollisionableActor {
    private final Animation<TextureRegion> noMasksAnimation;
    private final Animation<TextureRegion> fewMasksAnimation;
    private final Animation<TextureRegion> manyMasksAnimation;
    private final LaPandemia game;
    private final GameScreen gameScreen;

    // Cada cuánto se incrementa el nivel de salud (segundos) y nivel máximo.
    private static final float HEALTH_TICK = 1;
    private static final int MAX_HEALTH = 4;

    // -1: izquierda/abajo, 0: quieto, 1: derecha/arriba.
    private int xDir;
    private int yDir;

    // Tiempo transcurrido desde la última llamada a draw(), necesario para
    // animar el sprite.
    private float elapsedTime;
    private float speed;

    // Mascarillas recolectadas.
    private int maskCount;

    // Salud y cuánto tiempo ha pasado desde su último incremento o decremento.
    private int health;
    private float healthTime;

    // Cantidad de rollos de papel recogidos (equivalentes a puntos).
    private int paperCount;

    // Para evitar que collidedWith gestione dos veces la colisión simultánea con
    // dos WallActors.
    private boolean wallCollisionSeen;

    public PlayerActor(final LaPandemia game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        setSize(64, 64);
        setDirection(0, 0);
        elapsedTime = 0;
        healthTime = 0;
        speed = 400;
        health = MAX_HEALTH;

        noMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("player-no-masks.pack")).getRegions());
        fewMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("player-few-masks.pack")).getRegions());
        manyMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("player-many-masks.pack")).getRegions());

        maskCount = 0;
        paperCount = 0;

        setTouchable(Touchable.enabled);
    }

    public void setMaskCount(int maskCount) {
        this.maskCount = maskCount;
    }

    public int getMaskCount() {
        return maskCount;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDirection(int xDir, int yDir) {
        if (xDir < -1 || xDir > 1 || yDir < -1 || yDir > 1) {
            throw new IllegalArgumentException();
        }

        this.xDir = xDir;
        this.yDir = yDir;
    }

    public boolean isAlive() {
        return maskCount >= 0 && health > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> animation =
                maskCount <= 0
                        ? noMasksAnimation
                        : (maskCount < 3) ? fewMasksAnimation : manyMasksAnimation;
        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                getX(), getY());
    }

    @Override
    public void act(float delta) {
        wallCollisionSeen = false;

        if (!isAlive()) {
            // Por lo de pronto tan solo se para de mover el personaje. Más adelante se
            // implementará la lógica real del fin de la partida.
            return;
        }

        healthTime += delta;
        if (healthTime > HEALTH_TICK) {
            healthTime = 0;
            health = Math.min(MAX_HEALTH, health + 1);
        }

        int xDisplacement = (int)Math.floor(speed*delta*xDir);
        int yDisplacement = (int)Math.floor(speed*delta*yDir);
        collisionDispatcher.tryMove(this, xDisplacement, yDisplacement);
    }

    @Override
    public void collidedWith(CollisionableActor actor, ActorId id, int srcX, int srcY) {
        switch (id) {
            case WALL:
                if (!wallCollisionSeen) {
                    wallCollisionSeen = true;
                    setDirection(-xDir, -yDir);
                    healthTime = 0;
                    health--;
                }
                break;

            case FAN:
                health = 0;
                break;

            case MASK:
                maskCount++;
                break;

            case VIRUS:
                infected();
                break;

            case PAPER:
                paperCount++;
                break;
        }
    }

    @Override
    public void collidedBy(CollisionableActor actor, ActorId id, int srcX, int srcY) {
        if (id == ActorId.VIRUS) {
            infected();
        }
    }

    private void infected() {
        maskCount--;
    }
}
