package com.colegiovivas.lapandemia.gameplay.generator;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.GenerableActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class ActorGeneratorFactory {
    private final GameScreen gameScreen;
    private final LaPandemia game;

    public ActorGeneratorFactory(GameScreen gameScreen, LaPandemia game)
    {
        this.gameScreen = gameScreen;
        this.game = game;
    }

    public ActorGenerator getInstance(float tick, float width, float height, Float maxCount,
                                      Pool<GenerableActor> generableActorPool) {
        ActorGenerator actorGenerator = new ActorGenerator(tick, width, height, maxCount, generableActorPool);
        actorGenerator.setGameScreen(gameScreen);
        actorGenerator.setGame(game);
        return actorGenerator;
    }
}
