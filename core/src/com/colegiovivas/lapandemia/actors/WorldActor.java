package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.screens.game.World;

public class WorldActor extends Actor {
    private World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
