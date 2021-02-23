package com.colegiovivas.lapandemia.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.screens.WorldSubscreen;

public class WorldActor extends Actor {
    private WorldSubscreen worldSubscreen;

    public void setWorldSubscreen(WorldSubscreen worldSubscreen) {
        this.worldSubscreen = worldSubscreen;
    }

    public WorldSubscreen getWorldSubscreen() {
        return worldSubscreen;
    }
}
