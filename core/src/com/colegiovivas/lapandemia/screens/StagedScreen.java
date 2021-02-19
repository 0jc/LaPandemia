package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

public class StagedScreen implements Screen {
    private Array<Subscreen> subscreens;
    private GameStage gameStage;

    public StagedScreen() {
        subscreens = new Array<>();
    }

    @Override
    public void show() {
        gameStage.show();
    }

    @Override
    public void render(float delta) {
        gameStage.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        for (Subscreen subscreen : subscreens) {
            subscreen.resize(width, height);
        }
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
        for (Subscreen subscreen : subscreens) {
            subscreen.dispose();
        }
        subscreens.clear();
    }

    protected void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
        show();
    }

    protected void addSubscreen(Subscreen subscreen) {
        this.subscreens.add(subscreen);
    }

    public abstract static class GameStage {
        void show() {
            Gdx.input.setInputProcessor(new InputAdapter());
        }

        abstract void render(float delta);
    }
}
