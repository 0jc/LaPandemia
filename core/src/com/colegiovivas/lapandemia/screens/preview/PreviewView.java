package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

/**
 * Vista de la pantalla de previsualización de un mapa.
 */
public class PreviewView {
    private final Stage stage;
    private final TextButton startButton;

    public PreviewView(AssetManager assetManager, LevelInfo levelInfo) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = assetManager.get("cloud-form-skin/cloud-form-ui.json");
        TextureRegion whitePixel = ((TextureAtlas)assetManager.get("images.pack")).findRegion("ui-whitepixel");
        Label levelNameLabel = new Label(levelInfo.getName(), cloudFormSkin, "title");
        Label scoreLabel = new Label("Rollos de papel: " + levelInfo.getHighscore().getScore(), cloudFormSkin);
        Label authorLabel = new Label("Autor: " + levelInfo.getHighscore().getAuthor(), cloudFormSkin);
        startButton = new TextButton("COMENZAR", cloudFormSkin);

        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(whitePixel, Color.SKY));
        table.setFillParent(true);
        table.left().top();

        Table levelNameTable = new Table();
        levelNameTable.setBackground(new MonochromaticDrawable(whitePixel, Color.OLIVE));
        table.add(levelNameTable).pad(10).expandX().fillX().row();

        levelNameTable.pad(10);
        levelNameTable.add(levelNameLabel).expandX().left();

        Table statsTable = new Table();
        statsTable.center();
        statsTable.setBackground(new MonochromaticDrawable(whitePixel, Color.WHITE));
        table.add(statsTable).padLeft(10).padRight(10).padBottom(10).expand().fill().row();

        statsTable.add(scoreLabel).expandX().padBottom(10).row();
        statsTable.add(authorLabel).expandX().row();

        Table startButtonTable = new Table();
        table.add(startButtonTable).padLeft(10).padRight(10).padBottom(10).expandX().fillX();
        startButtonTable.add(startButton).expandX().fillX();

        stage.addActor(table);
    }

    public void addStartListener(EventListener eventListener) {
        startButton.addListener(eventListener);
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}
