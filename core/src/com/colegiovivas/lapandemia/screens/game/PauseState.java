package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que la partida se encuentra en pausa.
 */
public class PauseState implements MultistateScreen.State {
    private final GameScreen gameScreen;

    /**
     * True si todavía no se ha terminado de realizar el gesto de pausa de
     * la partida. Útil para distinguir, al detectar el gesto, si se debe
     * interpretar como una indicación para reanudar la partida.
     */
    private boolean pausing;

    /**
     * Si se ha empezado ya el gesto de reanudación de la partida.
     */
    private boolean resume;

    public PauseState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        pausing = true;
        resume = false;
        gameScreen.getWorld().setPaused(true);
        gameScreen.getStats().setPaused(true);
        gameScreen.getPauseSound().play();
    }

    @Override
    public void leave() {
        gameScreen.getPauseSound().stop();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        if (pausing) {
            if (Gdx.input.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                pausing = false;
            }
        } else if (!resume){
            if (Gdx.input.getGyroscopeY() < GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                resume = true;
            }
        } else if (Gdx.input.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
            gameScreen.setState(GameScreen.STAGE_PLAYING);
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
