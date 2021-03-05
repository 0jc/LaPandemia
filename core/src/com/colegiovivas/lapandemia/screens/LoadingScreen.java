package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

/**
 * Pantalla de carga de los assets del juego. Cuando se lanza, se cargan todos los
 * assets del assetManager, mostrando mientras en pantalla el porcentaje de progreso.
 * Una vez se ha completado la carga, se notifica a la clase principal para que
 * determine a qué pantalla cambiar.
 */
public class LoadingScreen implements Screen {
    /**
     * Batch de Libgdx que se utiliza para dibujar en pantalla el porcentaje de carga.
     */
    private final SpriteBatch batch;
    private final LaPandemia parent;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final GlyphLayout loadingTitleLayout;
    private final GlyphLayout loadedPercentLayout;
    private final InputProcessor noInput;

    /**
     * Espacio entre el texto "Cargando..." y el porcentaje de carga.
     */
    private static final float LINE_SPACING = 40;

    public LoadingScreen(final LaPandemia parent) {
        this.parent = parent;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);
        loadingTitleLayout = new GlyphLayout();
        loadedPercentLayout = new GlyphLayout();
        noInput = new InputAdapter();
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(noInput);

        // Lo imprescindible para poder mostrar esta misma pantalla.
        // finishLoading() se asegura de cargar todos estos recursos antes de devolver.
        parent.assetManager.load("fonts/nice32.fnt", BitmapFont.class);
        parent.assetManager.finishLoading();

        // Todos los demás recursos se irán cargando después gradualmente.
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
        parent.assetManager.load("audio/stat-reached-its-value.wav", Music.class);
        parent.assetManager.load("audio/stat-shown.wav", Music.class);
        parent.assetManager.load("audio/claps.wav", Music.class);
        parent.assetManager.load("audio/ticking.wav", Music.class);
        parent.assetManager.load("audio/direction-turn.wav", Music.class);
        parent.assetManager.load("audio/game-opening.wav", Music.class);
        parent.assetManager.load("audio/map.wav", Music.class);
        parent.assetManager.load("audio/game-over.wav", Music.class);
        parent.assetManager.load("audio/results.wav", Music.class);
        parent.assetManager.load("audio/menu-misc.mp3", Music.class);
        parent.assetManager.load("cloud-form-skin/cloud-form-ui.json", Skin.class);

        loadingTitleLayout.setText(
                (BitmapFont)parent.assetManager.get("fonts/nice32.fnt"), "Cargando...");
    }

    @Override
    public void render(float delta) {
        if (parent.assetManager.update()) {
            // Todos los recursos han sido cargados ya.
            parent.resourcesLoaded(this);
            return;
        }

        BitmapFont nice32 = (BitmapFont)parent.assetManager.get("fonts/nice32.fnt");

        loadedPercentLayout.setText(
                nice32, String.format("%.0f%%", parent.assetManager.getProgress() * 100));

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Situamos ambas líneas de texto de forma que el rectángulo formado por estas dos
        // y su espacio intermedio se muestre centrado en la pantalla.
        nice32.draw(
                batch,
                loadingTitleLayout,
                -loadingTitleLayout.width / 2,
                -loadingTitleLayout.height + (LINE_SPACING + loadedPercentLayout.height) / 2);
        nice32.draw(
                batch,
                loadedPercentLayout,
                -loadedPercentLayout.width / 2,
                -(loadingTitleLayout.height + LINE_SPACING + loadedPercentLayout.height) / 2);
        batch.end();
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
        batch.dispose();
    }
}
