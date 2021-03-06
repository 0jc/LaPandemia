package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

public class IdleState implements MultistateScreen.State {
    private final LaPandemia main;
    private final PreviewScreen previewScreen;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public IdleState(LaPandemia main, final PreviewScreen previewScreen) {
        this.main = main;
        this.previewScreen = previewScreen;
        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(previewScreen.getPreviewView().getStage());
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    previewScreen.back();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void enter() {
    }

    @Override
    public void leave() {
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
        Gdx.input.setInputProcessor(noInput);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        previewScreen.draw();
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
