package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class ResultsScreen implements Screen {
    private final LaPandemia parent;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    public ResultsScreen(LaPandemia parent) {
        this.parent = parent;
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
