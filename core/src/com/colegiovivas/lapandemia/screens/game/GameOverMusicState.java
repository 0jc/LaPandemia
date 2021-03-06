package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se reproduce la música de fin de la partida.
 */
public class GameOverMusicState implements MultistateScreen.State {
    private final GameScreen gameScreen;

    public GameOverMusicState(LaPandemia main, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        gameScreen.getMapMusic().stop();
        gameScreen.getGameOverMusic().setLooping(false);
        gameScreen.getGameOverMusic().play();
    }

    @Override
    public void leave() {

    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        if (!gameScreen.getGameOverMusic().isPlaying()) {
            gameScreen.setState(GameScreen.STATE_CLOSING);
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
