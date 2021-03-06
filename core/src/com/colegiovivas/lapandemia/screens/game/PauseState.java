package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que la partida se encuentra en pausa.
 */
public class PauseState implements MultistateScreen.State {
    private final LaPandemia main;
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

    /**
     * Procesador de la tecla de retroceso, para utilizarla como botón de
     * restauración de la partida.
     */
    private final InputProcessor backButtonProcessor;

    /**
     * Procesador para no capturar la entrada de datos al salir de este estado.
     */
    private final InputProcessor noInput;

    public PauseState(LaPandemia main, GameScreen gameScreen) {
        this.main = main;
        this.gameScreen = gameScreen;
        this.noInput = new InputAdapter();
        this.backButtonProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    resumeGame();
                    return true;
                }

                return false;
            }
        };
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
        Gdx.input.setInputProcessor(noInput);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(backButtonProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        if (pausing) {
            if (main.hardwareWrapper.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                pausing = false;
            }
        } else if (!resume){
            if (main.hardwareWrapper.getGyroscopeY() < GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
                resume = true;
            }
        } else if (main.hardwareWrapper.getGyroscopeY() >= GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
            resumeGame();
        }
    }

    /**
     * Restablece el estado de la pantalla al estado de juego.
     */
    private void resumeGame() {
        gameScreen.setState(GameScreen.STATE_PLAYING);
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
