package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

/**
 * Pantalla de carga de los assets del juego. Cuando se lanza, se cargan todos los
 * assets del assetManager, mostrando mientras en pantalla el porcentaje de progreso.
 * Una vez se ha completado la carga, se notifica a la clase principal para que
 * determine a qué pantalla cambiar.
 */
public class LoadingScreen extends ScreenAdapter {
    /**
     * Batch que se utiliza para dibujar en pantalla el porcentaje de carga.
     */
    private final SpriteBatch batch;

    /**
     * Pantalla principal a la que se notifica cuando los recursos han sido cargados.
     */
    private final LaPandemia main;

    /**
     * Gestor de recursos que se carga completamente con todos los recursos del juego.
     */
    private final AssetManager assetManager;

    /**
     * Viewport de la cámara de la pantalla.
     */
    private final Viewport viewport;

    /**
     * Cámara que muestra la interfaz de la pantalla.
     */
    private final OrthographicCamera camera;

    /**
     * Layout de texto que se utiliza para mostrar el texto "Cargando...".
     */
    private final GlyphLayout loadingTitleLayout;

    /**
     * Layout de texto que se utiliza para mostrar el porcentaje de carga de los recursos.
     */
    private final GlyphLayout loadedPercentLayout;

    /**
     * Procesador de entrada de datos que se establece para no capturar datos.
     */
    private final InputProcessor noInput;

    /**
     * Espacio entre el texto "Cargando..." y el porcentaje de carga.
     */
    private static final float LINE_SEP = 40;

    /**
     * Prepara los recursos necesarios para el funcionamiento de la pantalla.
     * @param main Valor para {@link #main}.
     * @param assetManager Valor para {@link #assetManager}.
     */
    public LoadingScreen(LaPandemia main, AssetManager assetManager) {
        this.main = main;
        this.assetManager = assetManager;

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
        assetManager.load("fonts/nice32.fnt", BitmapFont.class);
        assetManager.finishLoading();

        // Todos los demás recursos se irán cargando después gradualmente.
        assetManager.load("images.pack", TextureAtlas.class);
        assetManager.load("audio/countdown-beep-number.wav", Sound.class);
        assetManager.load("audio/countdown-beep-go.wav", Sound.class);
        assetManager.load("audio/hit-fan.wav", Sound.class);
        assetManager.load("audio/hit-wall.wav", Sound.class);
        assetManager.load("audio/infected.wav", Sound.class);
        assetManager.load("audio/mask-collected.wav", Sound.class);
        assetManager.load("audio/toilet-paper-collected.wav", Sound.class);
        assetManager.load("audio/pause.wav", Sound.class);
        assetManager.load("audio/virus-killed.wav", Sound.class);
        assetManager.load("audio/stat-reached-its-value.wav", Music.class);
        assetManager.load("audio/stat-shown.wav", Music.class);
        assetManager.load("audio/claps.wav", Music.class);
        assetManager.load("audio/ticking.wav", Music.class);
        assetManager.load("audio/game-opening.wav", Music.class);
        assetManager.load("audio/map.wav", Music.class);
        assetManager.load("audio/game-over.wav", Music.class);
        assetManager.load("audio/results.wav", Music.class);
        assetManager.load("audio/menu-misc.mp3", Music.class);
        assetManager.load("audio/level-chosen.wav", Music.class);
        assetManager.load("cloud-form-skin/cloud-form-ui.json", Skin.class);

        loadingTitleLayout.setText(
                (BitmapFont)assetManager.get("fonts/nice32.fnt"), "Cargando...");
    }

    @Override
    public void render(float delta) {
        if (assetManager.update()) {
            // Todos los recursos han sido cargados ya.
            main.resourcesLoaded(this);
            return;
        }

        BitmapFont nice32 = (BitmapFont)assetManager.get("fonts/nice32.fnt");

        loadedPercentLayout.setText(
                nice32, String.format("%.0f%%", assetManager.getProgress() * 100));

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
                -loadingTitleLayout.height + (LINE_SEP + loadedPercentLayout.height) / 2);
        nice32.draw(
                batch,
                loadedPercentLayout,
                -loadedPercentLayout.width / 2,
                -(loadingTitleLayout.height + LINE_SEP + loadedPercentLayout.height) / 2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
