package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.CollisionInfo;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class PlayerActor extends Actor {
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

    // Será false cuando el personaje se muera, es decir, cuando se termine la partida.
    private boolean alive;

    // Salud y cuánto tiempo ha pasado desde su último incremento o decremento.
    private int health;
    private float healthTime;

    public PlayerActor(float x, float y, final LaPandemia game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        setBounds(x, y, 64, 64);
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
        alive = true;

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
        if (!alive) {
            // Por lo de pronto tan solo se para de mover el personaje. Más adelante se
            // implementará la lógica real del fin de la partida.
            return;
        }

        healthTime += delta;
        if (healthTime > HEALTH_TICK) {
            healthTime = 0;
            health = Math.min(MAX_HEALTH, health + 1);
        }

        float xDisplacement = speed*delta*xDir;
        float yDisplacement = speed*delta*yDir;
        CollisionInfo collisionInfo = game.collisionInfoPool.obtain().init(
                this, xDisplacement, yDisplacement);
        try {
            setPosition(
                    getX() + collisionInfo.effectiveXDisplacement,
                    getY() + collisionInfo.effectiveYDisplacement);
            if (collisionInfo.walls.size != 0) {
                setDirection(-xDir, -yDir);

                healthTime = 0;
                health--;
                if (health == 0) {
                    alive = false;
                }
                return;
            }
            if (collisionInfo.fans.size != 0) {
                alive = false;
                return;
            }

            for (int i = 0; i < collisionInfo.masks.size; i++) {
                MaskActor mask = (MaskActor)collisionInfo.masks.get(i);
                collectMask(mask);
            }
            for (int i = 0; i < collisionInfo.viruses.size; i++) {
                VirusActor virus = (VirusActor)collisionInfo.viruses.get(i);
                infect(virus);
            }
        } finally {
            game.collisionInfoPool.free(collisionInfo);
        }
    }

    public void infect(VirusActor virus) {
        virus.remove();
        maskCount--;
        if (maskCount < 0) {
            alive = false;
        }
    }

    public void collectMask(MaskActor mask) {
        mask.remove();
        maskCount++;
    }
}
