package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se reproduce la transición final.
 */
public class ClosingState extends MultistateScreen.StateAdapter {
    private final GameScreen gameScreen;

    public ClosingState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        gameScreen.getClosingTransition().start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.getClosingTransition().render(delta);

        gameScreen.draw();
        gameScreen.getClosingTransition().draw();

        if (gameScreen.getClosingTransition().isComplete()) {
            gameScreen.finishGame();
        }
    }
}
