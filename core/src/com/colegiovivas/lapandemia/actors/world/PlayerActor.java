package com.colegiovivas.lapandemia.actors.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionableActor;

public class PlayerActor extends CollisionableActor {
    private final Animation<TextureRegion> defaultAnimation;
    private final Animation<TextureRegion> invincibleAnimation;
    private final LaPandemia game;
    private final Music turnSound;
    private PowerupListener powerupListener;
    private InvincibilityListener invincibilityListener;

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

    // Durante cuánto tiempo será invencible (0: no invencible).
    private float invincibilityTimeLeft;
    // Tiempo total de invencibilidad por jeringuilla.
    private static final float INVINCIBILITY_TIMESPAN = 10;

    private HealthActor healthActor;

    // Para evitar que se gestione dos veces la colisión simultánea con
    // dos WallActors o FanActors.
    private boolean wallCollisionSeen;
    private boolean fanCollisionSeen;

    public PlayerActor(final LaPandemia game) {
        this.game = game;
        setSize(64, 64);
        setDirection(0, 0);
        elapsedTime = 0;
        healthTime = 0;
        speed = 400;
        health = MAX_HEALTH;

        defaultAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("player-default"));
        invincibleAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("player-invincible"));

        maskCount = 0;
        paperCount = 0;
        invincibilityTimeLeft = 0;

        setTouchable(Touchable.enabled);

        turnSound = game.assetManager.get("audio/direction-turn.wav");
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

    public int getPaperCount() {
        return paperCount;
    }

    public void setHealthActor(HealthActor healthActor) {
        this.healthActor = healthActor;
    }

    public void setPowerupListener(PowerupListener powerupListener) {
        this.powerupListener = powerupListener;
    }

    public void setInvincibilityListener(InvincibilityListener invincibilityListener) {
        this.invincibilityListener = invincibilityListener;
    }

    public void setDirection(int xDir, int yDir) {
        if (xDir < -1 || xDir > 1 || yDir < -1 || yDir > 1) {
            throw new IllegalArgumentException();
        }

        this.xDir = xDir;
        this.yDir = yDir;
    }

    public void turn(int xDir, int yDir) {
        if (xDir != this.xDir || yDir != this.yDir) {
            //turnSound.play();
            setDirection(xDir, yDir);
        }
    }

    public boolean isAlive() {
        return maskCount >= 0 && health > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += getWorldSubscreen().getPaused() ? 0 : Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> animation =
                invincibilityTimeLeft <= 0
                        ? defaultAnimation
                        : invincibleAnimation;
        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        wallCollisionSeen = false;
        fanCollisionSeen = false;

        if (!isAlive()) {
            healthActor.showHealth(false);
            // Por lo de pronto tan solo se para de mover el personaje. Más adelante se
            // implementará la lógica real del fin de la partida.
            return;
        }

        healthTime += delta;
        if (healthTime > HEALTH_TICK && health < 4) {
            healthTime = 0;
            health++;
            healthActor.setHealth(health);
        }

        if (invincibilityTimeLeft > 0) {
            invincibilityTimeLeft = Math.max(0, invincibilityTimeLeft - delta);
        }

        int xDisplacement = (int)Math.floor(speed*delta*xDir);
        int yDisplacement = (int)Math.floor(speed*delta*yDir);
        collisionDispatcher.tryMove(this, xDisplacement, yDisplacement);
    }

    @Override
    public void collidedWith(CollisionableActor actor) {
        switch (actor.getActorId()) {
            case WALL:
                if (!wallCollisionSeen) {
                    wallCollisionSeen = true;
                    if (!fanCollisionSeen) setDirection(-xDir, -yDir);
                    healthTime = 0;
                    health--;
                    if (health == 0 && maskCount > 0) {
                        maskCount--;
                        powerupListener.updateCount(ActorId.MASK, maskCount);
                        health = MAX_HEALTH;
                    }
                    healthActor.setHealth(health);
                }
                break;

            case FAN:
                if (!fanCollisionSeen) {
                    fanCollisionSeen = true;
                    if (!wallCollisionSeen) setDirection(-xDir, -yDir);
                    maskCount--;
                    powerupListener.updateCount(ActorId.MASK, maskCount);
                }
                break;

            case MASK:
                maskCount++;
                powerupListener.updateCount(ActorId.MASK, maskCount);
                break;

            case VIRUS:
                infected();
                break;

            case PAPER:
                paperCount++;
                powerupListener.updateCount(ActorId.PAPER, paperCount);
                break;

            case NEEDLE:
                invincibilityTimeLeft += INVINCIBILITY_TIMESPAN;
                invincibilityListener.updateTimer(invincibilityTimeLeft);
                break;
        }
    }

    @Override
    public ActorId getActorId() {
        return ActorId.PLAYER;
    }

    @Override
    public void collidedBy(CollisionableActor actor) {
        if (actor.getActorId() == ActorId.VIRUS) {
            infected();
        }
    }

    private void infected() {
        if (invincibilityTimeLeft == 0) {
            maskCount--;
            powerupListener.updateCount(ActorId.MASK, maskCount);
        }
    }

    public interface PowerupListener {
        void updateCount(ActorId powerupId, int total);
    }

    public interface InvincibilityListener {
        void updateTimer(float total);
    }
}
