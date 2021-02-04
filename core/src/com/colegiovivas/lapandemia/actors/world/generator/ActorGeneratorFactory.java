package com.colegiovivas.lapandemia.actors.world.generator;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.GenerableActor;
import com.colegiovivas.lapandemia.screens.GameScreen;
import com.colegiovivas.lapandemia.screens.WorldSubscreen;

public class ActorGeneratorFactory {
    private final WorldSubscreen worldSubscreen;
    private final LaPandemia game;

    public ActorGeneratorFactory(WorldSubscreen gameScreen, LaPandemia game)
    {
        this.worldSubscreen = gameScreen;
        this.game = game;
    }

    public ActorGenerator getInstance(Class<? extends GenerableActor> generableActorClass, ActorId actorId,
            Group destGroup, float width, float height, float tick, Integer maxCount, Float ttl)
    {
        ActorGenerator actorGenerator = new ActorGenerator(
                generableActorClass, actorId, destGroup, width, height, tick, maxCount, ttl);
        actorGenerator.setWorldSubscreen(worldSubscreen);
        actorGenerator.setGame(game);
        return actorGenerator;
    }
}
