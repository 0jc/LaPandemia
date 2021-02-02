package com.colegiovivas.lapandemia.actors.generator;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.ActorId;
import com.colegiovivas.lapandemia.actors.GenerableActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class ActorGeneratorFactory {
    private final GameScreen gameScreen;
    private final LaPandemia game;

    public ActorGeneratorFactory(GameScreen gameScreen, LaPandemia game)
    {
        this.gameScreen = gameScreen;
        this.game = game;
    }

    public ActorGenerator getInstance(Class<? extends GenerableActor> generableActorClass, ActorId actorId,
                                      Group destGroup, float width, float height, float tick, Integer maxCount, Float ttl)

    {
        ActorGenerator actorGenerator = new ActorGenerator(
                generableActorClass, actorId, destGroup, width, height, tick, maxCount, ttl);
        actorGenerator.setGameScreen(gameScreen);
        actorGenerator.setGame(game);
        return actorGenerator;
    }
}
