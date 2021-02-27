package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que la pantalla se encuentra en espera, por motivos de
 * presentación.
 */
public class WaitState implements MultistateScreen.State {
    private final GameScreen gameScreen;

    /**
     * Tiempo total de espera deseado.
     */
    private final float totalTime;

    /**
     * Estado al que se salta una vez se termina el tiempo de espera.
     */
    private final int nextState;

    /**
     * Tiempo que se lleva esperado.
     */
    private float waitedTime;

    public WaitState(GameScreen gameScreen, float totalTime, int nextState) {
        this.gameScreen = gameScreen;
        this.totalTime = totalTime;
        this.nextState = nextState;
    }

    @Override
    public void enter() {
        waitedTime = 0;
    }

    @Override
    public void leave() {

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        waitedTime += delta;
        if (waitedTime >= totalTime) {
            gameScreen.setState(nextState);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
