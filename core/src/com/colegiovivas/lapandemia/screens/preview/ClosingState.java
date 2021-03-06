package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

public class ClosingState implements MultistateScreen.State {
    private final LaPandemia main;
    private final PreviewScreen previewScreen;

    public ClosingState(LaPandemia main, PreviewScreen previewScreen) {
        this.main = main;
        this.previewScreen = previewScreen;
    }

    @Override
    public void enter() {
        previewScreen.getClosingTransition().start();
        previewScreen.getBackgroundMusic().stop();
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

        previewScreen.getClosingTransition().render(delta);

        previewScreen.draw();

        if (previewScreen.getClosingTransition().isComplete()) {
            previewScreen.play();
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
