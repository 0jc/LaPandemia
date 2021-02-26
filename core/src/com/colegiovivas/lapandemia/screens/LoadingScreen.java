package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
    private static final float LINE_SPACING = 40;

    public LoadingScreen(final LaPandemia parent) {
        this.parent = parent;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);
        loadingTitleLayout = new GlyphLayout();
        loadedPercentLayout = new GlyphLayout();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter());

        // Lo básico para poder mostrar esta misma pantalla.
        parent.assetManager.load("fonts/nice32.fnt", BitmapFont.class);
        parent.assetManager.finishLoading();

        parent.assetManager.load("images.pack", TextureAtlas.class);
        parent.assetManager.load("audio/countdown-beep-number.wav", Sound.class);
        parent.assetManager.load("audio/countdown-beep-go.wav", Sound.class);
        parent.assetManager.load("audio/hit-fan.wav", Sound.class);
        parent.assetManager.load("audio/hit-wall.wav", Sound.class);
        parent.assetManager.load("audio/infected.wav", Sound.class);
        parent.assetManager.load("audio/mask-collected.wav", Sound.class);
        parent.assetManager.load("audio/toilet-paper-collected.wav", Sound.class);
        parent.assetManager.load("audio/pause.wav", Sound.class);
        parent.assetManager.load("audio/virus-killed.wav", Sound.class);
        parent.assetManager.load("audio/ticking.wav", Music.class);
        parent.assetManager.load("audio/direction-turn.wav", Music.class);
        parent.assetManager.load("audio/game-opening.wav", Music.class);
        parent.assetManager.load("audio/map.wav", Music.class);
        parent.assetManager.load("audio/game-over.wav", Music.class);
        parent.assetManager.load("audio/results.wav", Music.class);

        loadingTitleLayout.setText(
                (BitmapFont)parent.assetManager.get("fonts/nice32.fnt"), "Cargando...");
    }

    @Override
    public void render(float delta) {
        if (parent.assetManager.update()) {
            parent.resourcesLoaded(this);
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
                -loadingTitleLayout.height + (LINE_SPACING + loadedPercentLayout.height) / 2);
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
