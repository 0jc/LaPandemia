package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class CountdownGameStage implements StagedScreen.GameStage {
    private final GameScreen gameScreen;

    public CountdownGameStage(LaPandemia main, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        gameScreen.getCountdown().start();
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

        gameScreen.getCountdown().render(delta);

        gameScreen.draw();
        gameScreen.drawCountdown();

        if (!gameScreen.getCountdown().isCountingDown()) {
            gameScreen.setGameStage(GameScreen.STAGE_PLAYING);
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
