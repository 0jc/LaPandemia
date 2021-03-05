package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

public class MainScreen implements Screen {
    private final LaPandemia main;
    private final Stage stage;

    public MainScreen(LaPandemia main) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");

        TextButton playButton = new TextButton("Jugar", cloudFormSkin);
        TextButton settingsButton = new TextButton("Ajustes", cloudFormSkin);
        TextButton creditsButton = new TextButton("Agradecimientos", cloudFormSkin);
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
                MainScreen.this.main.mapSelectionScreenChosen(MainScreen.this);
            }
        });

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
