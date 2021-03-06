package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

/**
 * Vista de la pantalla del menú principal.
 */
public class MainMenuView {
    private final Stage stage;

    /**
     * Evento que se lanza cuando se elige la opción de mostrar la pantalla de selección de mapas.
     */
    private PlayListener playListener;

    /**
     * Evento que se lanza cuando se elige la opción de mostrar la pantalla de los créditos.
     */
    private CreditsListener creditsListener;

    /**
     * Evento que se lanza cuando se elige la opción de mostrar la pantalla de ajustes.
     */
    private SettingsListener settingsListener;

    public MainMenuView(LaPandemia main) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");

        final TextButton playButton = new TextButton("Mapas", cloudFormSkin);
        TextButton settingsButton = new TextButton("Ajustes", cloudFormSkin);
        final TextButton creditsButton = new TextButton("Agradecimientos", cloudFormSkin);
        Label headerLabel = new Label("LA PANDEMIA", cloudFormSkin);
        headerLabel.setFontScaleX(1.5f);
        headerLabel.setFontScaleY(1.5f);
        TextureAtlas atlas = main.assetManager.get("images.pack");
        Image virusImage = new Image(atlas.findRegion("main-menu-virus"));
        Image playerImage = new Image(atlas.findRegion("main-menu-player"));

        boolean debug = false;

        // La forma estándar de crear interfaces de usuario en Libgdx es mediante tablas,
        // que se pueden anidar entre ellas.
        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(main, Color.SKY));
        table.setFillParent(true);
        table.setDebug(debug);

        Table headerTable = new Table();
        headerTable.setBackground(new MonochromaticDrawable(main, Color.FOREST));
        headerTable.setDebug(debug);
        headerTable.add(virusImage).pad(10);
        headerTable.add(headerLabel).center().expandX().pad(10);
        headerTable.add(playerImage).pad(10).row();

        table.add(headerTable).expandX().fillX().padTop(10).padLeft(20).padRight(20).row();
        table.add(playButton).expand().fill().padTop(20).padBottom(5).padLeft(20).padRight(20).row();
        table.add(settingsButton).expand().fill().padBottom(5).padLeft(20).padRight(20).row();
        table.add(creditsButton).expand().fill().padBottom(10).padLeft(20).padRight(20).row();

        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playListener != null) playListener.playClicked();
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (creditsListener != null) creditsListener.creditsClicked();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (settingsListener != null) settingsListener.settingsClicked();
            }
        });

        stage.addActor(table);
    }

    /**
     * Establece el evento que se lanzará al elegir la opción de mostrar la pantalla de selección de mapas.
     * @param playListener Evento que será lanzado.
     */
    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    /**
     * Establece el evento que se lanzará al elegir la opción de mostrar la pantalla de los créditos.
     * @param creditsListener Evento que será lanzado.
     */
    public void setCreditsListener(CreditsListener creditsListener) {
        this.creditsListener = creditsListener;
    }

    /**
     * Establece el evento que se lanzará al elegir la opción de mostrar la pantalla de ajustes.
     * @param settingsListener Evento que será lanzado.
     */
    public void setSettingsListener(SettingsListener settingsListener) {
        this.settingsListener = settingsListener;
    }

    /**
     * @return El Stage de Libgdx al que pertenece la tabla con la interfaz de usuario de la vista.
     */
    public Stage getStage() {
        return stage;
    }

    public interface PlayListener {
        void playClicked();
    }

    public interface CreditsListener {
        void creditsClicked();
    }

    public interface SettingsListener {
        void settingsClicked();
    }

    /**
     * Libera los recursos utilizados por la vista.
     */
    public void dispose() {
        stage.dispose();
    }
}
