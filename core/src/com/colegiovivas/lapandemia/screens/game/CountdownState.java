package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se está llevando a cabo la cuenta atrás.
 */
public class CountdownState extends MultistateScreen.StateAdapter {
    private final GameScreen gameScreen;

    public CountdownState(LaPandemia main, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        gameScreen.getCountdown().start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.getCountdown().render(delta);

        gameScreen.draw();
        gameScreen.drawCountdown();

        if (!gameScreen.getCountdown().isCountingDown()) {
            gameScreen.setState(GameScreen.STATE_PLAYING);
        }
    }
}
