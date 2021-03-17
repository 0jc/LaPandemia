package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.screens.game.World;

/**
 * Actor que pertenece a un mapa de juego.
 */
public class WorldActor extends Actor {
    /**
     * Controlador del mundo al que pertence el actor.
     */
    private World world;

    /**
     * @param world Valor para {@link #world}.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * @return {@link #world}
     */
    public World getWorld() {
        return world;
    }
}
