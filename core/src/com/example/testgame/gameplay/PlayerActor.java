package com.example.testgame.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.example.testgame.MyTestGame;
import com.example.testgame.pooling.PoolableArray;
import com.example.testgame.pooling.PoolableRectangle;

public class PlayerActor extends Actor {
    private final Animation<TextureRegion> noMasksAnimation;
    private final Animation<TextureRegion> fewMasksAnimation;
    private final Animation<TextureRegion> manyMasksAnimation;
    private final MyTestGame game;

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

    public PlayerActor(float x, float y, final MyTestGame game) {
        this.game = game;
        setBounds(x, y, 64, 64);
        setDirection(0, 0);
        elapsedTime = 0;
        healthTime = 0;
        speed = 400;
        health = MAX_HEALTH;

        noMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) game.assetManager.get("player-no-masks.pack")).getRegions());
        fewMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) game.assetManager.get("player-few-masks.pack")).getRegions());
        manyMasksAnimation = new Animation<TextureRegion>(1f,
                ((TextureAtlas) game.assetManager.get("player-many-masks.pack")).getRegions());

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
                maskCount == 0
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
            Gdx.app.log("LaPandemia", "!alive");
            return;
        }

        healthTime += delta;
        if (healthTime > HEALTH_TICK) {
            healthTime = 0;
            health = Math.min(MAX_HEALTH, health + 1);
        }

        // Rectángulo del PlayerActor tras moverse a la siguiente posición.
        PoolableRectangle rPlayer = game.rectPool.obtain();
        // Rectángulo de otro actor para comprobar si hay colisión con rPlayer.
        PoolableRectangle rOther = game.rectPool.obtain();
        PoolableArray<Actor> collidingActors = game.actorArrayPool.obtain();
        try {
            rPlayer.set(getX() + speed*delta*xDir, getY() + speed*delta*yDir, getWidth(), getHeight());
            for (Actor actor : getStage().getActors()) {
                if (actor != this) {
                    rOther.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
                    if (rPlayer.overlaps(rOther)) {
                        float distance = getCollisionDistance(actor);
                        for (int i = collidingActors.size - 1; i >= 0; i--) {
                            if (getCollisionDistance(collidingActors.get(i)) > distance) {
                                // `actor` está físicamente antepuesto a `collidingActors.get(i)`,
                                // por lo que este último no es un candidato real a ser colisionado.
                                collidingActors.removeIndex(i);
                            }
                        }
                        collidingActors.add(actor);
                    }
                }
            }

            // El PlayerActor chocará simultáneamente con todos los miembros de collidingActors.
            WallActor bumpedWall = null;
            FanActor bumpedFan = null;
            for (Actor actor : collidingActors) {
                if (actor instanceof WallActor) {
                    bumpedWall = (WallActor)actor;
                }
                if (actor instanceof FanActor) {
                    bumpedFan = (FanActor)actor;
                }
            }

            if (bumpedWall != null && bumpedFan == null) {
                float spaceWithin = getCollisionDistance(bumpedWall) - 1;
                setPosition(getX() + xDir*spaceWithin, getY() + yDir*spaceWithin);
                setDirection(-xDir, -yDir);

                healthTime = 0;
                health--;
                if (health == 0) {
                    alive = false;
                }
            } else if (bumpedFan != null) {
                float spaceWithin = getCollisionDistance(bumpedFan) - 1;
                setPosition(getX() + xDir*spaceWithin, getY() + yDir*spaceWithin);
                alive = false;
            } else {
                setBounds(rPlayer.x, rPlayer.y, rPlayer.width, rPlayer.height);
            }
        } finally {
            game.rectPool.free(rPlayer);
            game.rectPool.free(rOther);
        }
    }

    // Obtener la cantidad mínima de píxeles que se deben recorrer para colisionar
    // con `actor`, dada la dirección actual del PlayerActor.
    private float getCollisionDistance(Actor actor) {
        if (xDir == -1) return getX() - (actor.getX() + actor.getWidth() - 1);
        if (xDir == 1) return actor.getX() - (getX() + getWidth() - 1);
        if (yDir == -1) return getY() - (actor.getY() + actor.getHeight() - 1);
        else return actor.getY() - (getY() + getHeight() - 1);
    }
}
