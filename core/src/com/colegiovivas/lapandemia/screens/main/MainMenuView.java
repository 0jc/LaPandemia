package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

/**
 * Vista de {@link MainMenuScreen}.
 */
public class MainMenuView {
    /**
     * Stage que contiene la interfaz de la vista.
     */
    private final Stage stage;

    /**
     * Botón de mostrar el menú de selección de mapas.
     */
    private final TextButton playButton;

    /**
     * Botón de mostrar el menú de ajustes.
     */
    private final TextButton settingsButton;

    /**
     * Botón de mostrar el menú de los créditos.
     */
    private final TextButton creditsButton;

    /**
     * Inicializa la pantalla.
     * @param assetManager Gestor de recursos de la aplicación, necesario para renderizar la pantalla.
     */
    public MainMenuView(AssetManager assetManager) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = assetManager.get("cloud-form-skin/cloud-form-ui.json");
        TextureRegion whitePixel = ((TextureAtlas)assetManager.get("images.pack")).findRegion("ui-whitepixel");

        playButton = new TextButton("Mapas", cloudFormSkin);
        settingsButton = new TextButton("Ajustes", cloudFormSkin);
        creditsButton = new TextButton("Agradecimientos", cloudFormSkin);
        Label headerLabel = new Label("LA PANDEMIA", cloudFormSkin);
        headerLabel.setFontScaleX(1.5f);
        headerLabel.setFontScaleY(1.5f);
        TextureAtlas atlas = assetManager.get("images.pack");
        Image virusImage = new Image(atlas.findRegion("main-menu-virus"));
        Image playerImage = new Image(atlas.findRegion("main-menu-player"));

        boolean debug = false;

        // La forma estándar de crear interfaces de usuario en Libgdx es mediante tablas,
        // que se pueden anidar entre ellas.
        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(whitePixel, Color.SKY));
        table.setFillParent(true);
        table.setDebug(debug);

        Table headerTable = new Table();
        headerTable.setBackground(new MonochromaticDrawable(whitePixel, Color.FOREST));
        headerTable.setDebug(debug);
        headerTable.add(virusImage).pad(10);
        headerTable.add(headerLabel).center().expandX().pad(10);
        headerTable.add(playerImage).pad(10).row();

        table.add(headerTable).expandX().fillX().padTop(10).padLeft(20).padRight(20).row();
        table.add(playButton).expand().fill().padTop(20).padBottom(5).padLeft(20).padRight(20).row();
        table.add(settingsButton).expand().fill().padBottom(5).padLeft(20).padRight(20).row();
        table.add(creditsButton).expand().fill().padBottom(10).padLeft(20).padRight(20).row();

        stage.addActor(table);
    }

    /**
     * Añade un EventListener a {@link #playButton}.
     * @param eventListener Listener que se añade a {@link #playButton}.
     */
    public void addPlayListener(EventListener eventListener) {
        playButton.addListener(eventListener);
    }

    /**
     * Añade un EventListener a {@link #settingsButton}.
     * @param eventListener Listener que se añade a {@link #settingsButton}.
     */
    public void addSettingsListener(EventListener eventListener) {
        settingsButton.addListener(eventListener);
    }

    /**
     * Añade un EventListener a {@link #creditsButton}.
     * @param eventListener Listener que se añade a {@link #creditsButton}.
     */
    public void addCreditsListener(EventListener eventListener) {
        creditsButton.addListener(eventListener);
    }

    /**
     * @return {@link #stage}
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Libera los recursos utilizados por la vista.
     */
    public void dispose() {
        stage.dispose();
    }
}
