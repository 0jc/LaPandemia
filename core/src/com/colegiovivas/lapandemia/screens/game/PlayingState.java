package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gestures.MovePlayerGestureListener;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que el usuario está jugando la partida.
 */
public class PlayingState implements MultistateScreen.State {
    private final LaPandemia main;
    private final GameScreen gameScreen;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public PlayingState(LaPandemia main, GameScreen gameScreen) {
        this.main = main;
        this.gameScreen = gameScreen;

        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        // ZoomGestureListener debe ir antes de MovePlayerGestureListener, ya que el primero
        // decide quién de los dos debe procesar los gestos tap.
        inputProcessor.addProcessor(new GestureDetector(new ZoomGestureListener(gameScreen.getWorld())));
        inputProcessor.addProcessor(new GestureDetector(new MovePlayerGestureListener(
                gameScreen.getWorld().getPlayerActor())));
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    pauseGame();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void enter() {
        gameScreen.getCurrentGameMusic().setLooping(true);
        gameScreen.getCurrentGameMusic().play();
        gameScreen.getWorld().setPaused(false);
        gameScreen.getStats().setPaused(false);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void leave() {
        gameScreen.getCurrentGameMusic().pause();
        Gdx.input.setInputProcessor(noInput);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.getWorld().render(delta);
        gameScreen.getStats().render(delta);
        gameScreen.getCountdown().render(delta);

        gameScreen.draw();
        gameScreen.drawCountdown();

        if (gameScreen.getWorld().gameIsOver()) {
            gameScreen.setState(GameScreen.STATE_GAME_OVER_MUSIC);
        } else if (main.hardwareWrapper.getGyroscopeY() < GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
            pauseGame();
        }
    }

    /**
     * Establece el estado de la pantalla al estado de pausado.
     */
    private void pauseGame() {
        gameScreen.setState(GameScreen.STATE_PAUSE);
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
