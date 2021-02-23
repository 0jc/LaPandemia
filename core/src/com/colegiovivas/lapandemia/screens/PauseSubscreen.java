package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class PauseSubscreen extends Subscreen{
    private final Stage stage;
    private Pixmap screenshotPixmap;
    private Texture screenshotTexture;
    private Image screenshotImage;

    public PauseSubscreen(LaPandemia game){
        Camera uiCamera = new OrthographicCamera();
        Viewport uiViewport = new StretchViewport(800, 480, uiCamera);
        stage = new Stage(uiViewport);
    }

    @Override
    protected void drawWithinBounds(float delta) {
        stage.draw();
    }

    @Override
    public void act(float delta) {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
