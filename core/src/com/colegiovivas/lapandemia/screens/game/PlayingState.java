package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
    private final GameScreen gameScreen;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public PlayingState(LaPandemia main, GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        // ZoomGestureListener debe ir antes de MovePlayerGestureListener, ya que el primero
        // decide quién de los dos debe procesar los gestos tap.
        inputProcessor.addProcessor(new GestureDetector(new ZoomGestureListener(gameScreen.getWorld())));
        inputProcessor.addProcessor(new GestureDetector(new MovePlayerGestureListener(
                gameScreen.getWorld().getPlayerActor())));
    }

    @Override
    public void enter() {
        gameScreen.getCurrentGameMusic().setLooping(true);
        gameScreen.getCurrentGameMusic().play();
        gameScreen.getWorld().setPaused(false);
        gameScreen.getStats().setPaused(false);
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
            gameScreen.setState(GameScreen.STAGE_GAME_OVER_MUSIC);
        } else if (Gdx.input.getGyroscopeY() < GameScreen.Y_GYROSCOPE_PAUSE_TRESHOLD) {
            gameScreen.setState(GameScreen.STAGE_PAUSE);
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
