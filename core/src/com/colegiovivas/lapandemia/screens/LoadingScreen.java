package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class LoadingScreen implements Screen {
    private final LaPandemia parent;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final GlyphLayout loadingTitleLayout;
    private final GlyphLayout loadedPercentLayout;

    // Espacio entre el texto "Cargando..." y el porcentaje de carga.
    private static final float LINE_SPACING = 20;

    public LoadingScreen(final LaPandemia parent) {
        this.parent = parent;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(LaPandemia.V_WIDTH, LaPandemia.V_HEIGHT, camera);
        loadingTitleLayout = new GlyphLayout();
        loadedPercentLayout = new GlyphLayout();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter());

        // Lo básico para poder mostrar esta misma pantalla.
        parent.assetManager.load("fonts/nice32.fnt", BitmapFont.class);
        parent.assetManager.finishLoading();

        parent.assetManager.load("player-default.pack", TextureAtlas.class);
        parent.assetManager.load("player-invincible.pack", TextureAtlas.class);
        parent.assetManager.load("virus.pack", TextureAtlas.class);
        parent.assetManager.load("fan.pack", TextureAtlas.class);
        parent.assetManager.load("health.pack", TextureAtlas.class);
        parent.assetManager.load("wall.png", Texture.class);
        parent.assetManager.load("mask.png", Texture.class);
        parent.assetManager.load("needle.png", Texture.class);
        parent.assetManager.load("toiletpaper.png", Texture.class);

        loadingTitleLayout.setText(
                (BitmapFont)parent.assetManager.get("fonts/nice32.fnt"), "Cargando...");
    }

    @Override
    public void render(float delta) {
        if (parent.assetManager.update()) {
            parent.resourcesLoaded();
            return;
        }

        BitmapFont nice32 = (BitmapFont)parent.assetManager.get("fonts/nice32.fnt");

        loadedPercentLayout.setText(
                nice32, String.format("%.0f%%", parent.assetManager.getProgress() * 100));

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        parent.batch.setProjectionMatrix(camera.combined);

        parent.batch.begin();
        nice32.draw(
                parent.batch,
                loadingTitleLayout,
                -loadingTitleLayout.width / 2,
                +(loadingTitleLayout.height + LINE_SPACING + loadedPercentLayout.height) / 2);
        nice32.draw(
                parent.batch,
                loadedPercentLayout,
                -loadedPercentLayout.width / 2,
                -(loadingTitleLayout.height + LINE_SPACING + loadedPercentLayout.height) / 2);
        parent.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
