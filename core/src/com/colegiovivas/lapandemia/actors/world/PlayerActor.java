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

/**
 * Actor del personaje principal.
 */
public class PlayerActor extends CollisionableActor {
    /**
     * Velocidad a la que se mueve el personaje.
     */
    private final float SPEED = 400;

    /**
     * Cada cuánto se incrementa el nivel de salud (en segundos).
     */
    private static final float HEALTH_TICK = 1;

    /**
     * Nivel máximo de salud alcanzable.
     */
    private static final int MAX_HEALTH = 4;

    /**
     * Tiempo de invencibilidad que proporciona cada vacuna.
     */
    private static final float INVINCIBILITY_TIMESPAN = 10;

    /**
     * Animación que el actor cobra por defecto.
     */
    private final Animation<TextureRegion> defaultAnimation;

    /**
     * Animación que el actor cobra al volverse invencible.
     */
    private final Animation<TextureRegion> invincibleAnimation;

    /**
     * Animación que el actor cobra al morirse.
     */
    private final Animation<TextureRegion> deadAnimation;

    private final LaPandemia game;

    /**
     * Sonido que se reproduce al cambiar de dirección (actualmente desactivado).
     */
    private final Music turnSound;

    /**
     * Sonido que se reproduce al chocar con un muro.
     */
    private final Sound wallHitSound;

    /**
     * Sonido que se reproduce al chocar con un ventilador.
     */
    private final Sound fanHitSound;

    /**
     * Sonido que se reproduce al ser infectado por un virus.
     */
    private final Sound infectionSound;

    /**
     * Sonido que se reproduce al capturar una mascarilla.
     */
    private final Sound maskSound;

    /**
     * Sonido que se reproduce al capturar un rollo de papel higiénico.
     */
    private final Sound paperSound;

    /**
     * Sonido que se reproduce al matar un virus.
     */
    private final Sound virusKilledSound;

    /**
     * Evento que se lanza al capturar un powerup (mascarilla, papel...).
     */
    private PowerupListener powerupListener;

    /**
     * Evento que se lanza al volverse invencible.
     */
    private InvincibilityListener invincibilityListener;

    /**
     * Dirección de desplazamiento sobre el eje X (-1 = izquierda, 1 = derecha, 0 = ninguna).
     */
    private int xDir;

    /**
     * Dirección de desplazamiento sobre el eje Y (-1 = abajo, 1 = arriba, 0 = ninguna).
     */
    private int yDir;

    /**
     * Tiempo transcurrido desde el inicio de la vida del personaje.
     */
    private float elapsedTime;

    /**
     * Mascarillas recolectadas.
     */
    private int maskCount;

    /**
     * Salud del personaje.
     */
    private int health;

    /**
     * Tiempo que ha pasado desde el último cambio de salud del personaje.
     */
    private float healthTime;

    /**
     * Cantidad de rollos de papel recogidos.
     */
    private int paperCount;

    /**
     * Tiempo restante para dejar de ser invencible (0: no invencible).
     */
    private float invincibilityTimeLeft;

    /**
     * Actor monitor de salud.
     */
    private HealthActor healthActor;

    /**
     * True si y solo si se ha gestionado una colisión con un muro en el frame actual.
     */
    private boolean wallCollisionSeen;

    /**
     * True si y solo si se ha gestionado una colisión con un ventilador en el frame actual.
     */
    private boolean fanCollisionSeen;

    public PlayerActor(final LaPandemia game) {
        this.game = game;
        setSize(64, 64);
        setDirection(0, 0);
        elapsedTime = 0;
        healthTime = 0;
        health = MAX_HEALTH;

        defaultAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("player-default"));
        invincibleAnimation = new Animation<TextureRegion>(0.2f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("player-invincible"));
        deadAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas)game.assetManager.get("images.pack")).findRegions("player-dead"));

        maskCount = 0;
        paperCount = 0;
        invincibilityTimeLeft = 0;

        setTouchable(Touchable.enabled);

        turnSound = game.assetManager.get("audio/direction-turn.wav");
        wallHitSound = game.assetManager.get("audio/hit-wall.wav");
        fanHitSound = game.assetManager.get("audio/hit-fan.wav");
        infectionSound = game.assetManager.get("audio/infected.wav");
        maskSound = game.assetManager.get("audio/mask-collected.wav");
        paperSound = game.assetManager.get("audio/toilet-paper-collected.wav");
        virusKilledSound = game.assetManager.get("audio/virus-killed.wav");
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

    /**
     * Establece la dirección del personaje en cada eje de coordenadas.
     * @param xDir -1 = izquierda, 1 = derecha, 0 = sin movimiento en el eje X.
     * @param yDir -1 = abajo, 1 = arriba, 0 = sin movimiento en el eje Y.
     */
    public void setDirection(int xDir, int yDir) {
        if (xDir < -1 || xDir > 1 || yDir < -1 || yDir > 1) {
            throw new IllegalArgumentException();
        }

        this.xDir = xDir;
        this.yDir = yDir;
    }

    /**
     * Realiza un cambio de dirección completo del personaje, no solo reestableciendo
     * el valor de esta sino también reproduciendo el sonido de cambio de dirección.
     * Se debe utilizar para cambios de dirección solicitados para el usuario, a
     * diferencia por ejemplo de los producidos por la colisión con un muro.
     * @param xDir Valor xDir para setDirection().
     * @param yDir Valor yDir para setDirection().
     */
    public void turn(int xDir, int yDir) {
        if (xDir != this.xDir || yDir != this.yDir) {
            // Actualmente, el sonido de cambio de dirección está desactivado.
            //turnSound.play();
            setDirection(xDir, yDir);
        }
    }

    /**
     * @return True si y solo si el personaje no ha muerto.
     */
    public boolean isAlive() {
        return maskCount >= 0 && health > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += getWorld().isPaused() ? 0 : Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> animation =
                isAlive()
                ? invincibilityTimeLeft <= 0
                  ? defaultAnimation
                  : invincibleAnimation
                : deadAnimation;
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
            if (invincibilityTimeLeft == 0) {
                if (invincibilityListener != null) invincibilityListener.stateChanged(false);
            }
        }

        int xDisplacement = (int)Math.floor(SPEED *delta*xDir);
        int yDisplacement = (int)Math.floor(SPEED *delta*yDir);
        collisionDispatcher.tryMove(this, xDisplacement, yDisplacement);
    }

    @Override
    public void collidedWith(CollisionableActor actor) {
        switch (actor.getActorId()) {
            case WALL:
                if (!wallCollisionSeen) {
                    wallHitSound.play();
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
                    fanHitSound.play();
                    fanCollisionSeen = true;
                    if (!wallCollisionSeen) setDirection(-xDir, -yDir);
                    maskCount--;
                    powerupListener.updateCount(ActorId.MASK, maskCount);
                }
                break;

            case MASK:
                maskSound.play();
                maskCount++;
                powerupListener.updateCount(ActorId.MASK, maskCount);
                break;

            case VIRUS:
                infected();
                break;

            case PAPER:
                paperSound.play();
                paperCount++;
                powerupListener.updateCount(ActorId.PAPER, paperCount);
                break;

            case NEEDLE:
                invincibilityTimeLeft += INVINCIBILITY_TIMESPAN;
                if (invincibilityListener != null) {
                    invincibilityListener.updateTimer(invincibilityTimeLeft);
                    if (invincibilityTimeLeft == INVINCIBILITY_TIMESPAN) {
                        invincibilityListener.stateChanged(true);
                    }
                }
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

    /**
     * Informa al actor de que se ha infectado.
     */
    private void infected() {
        if (invincibilityTimeLeft == 0) {
            infectionSound.play();
            Gdx.input.vibrate(100);
            maskCount--;
            powerupListener.updateCount(ActorId.MASK, maskCount);
        } else {
            virusKilledSound.play();
        }
    }

    public interface PowerupListener {
        /**
         * Notifica de un cambio en el número total de powerups de un tipo recolectados.
         * @param powerupId ID del powerup.
         * @param total Nueva cantidad total de powerups recolectados de este tipo.
         */
        void updateCount(ActorId powerupId, int total);
    }

    public interface InvincibilityListener {
        /**
         * Notifica de un cambio en el tiempo de invencibilidad originado por la
         * recolección de una vacuna.
         * @param total Nuevo valor de tiempo restante total.
         */
        void updateTimer(float total);

        /**
         * Notifica de un cambio entre los estados de invencibilidad y no invencibilidad.
         * @param invincible True si se ha vuelto invencible o false si se ha vuelto no invencible.
         */
        void stateChanged(boolean invincible);
    }
}
