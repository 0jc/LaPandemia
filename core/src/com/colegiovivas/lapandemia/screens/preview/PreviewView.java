package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class PreviewView {
    private final LaPandemia main;
    private final PreviewScreen previewScreen;
    private final LevelInfo levelInfo;
    private final Stage stage;

    private PlayListener playListener;

    public PreviewView(LaPandemia main, PreviewScreen previewScreen, LevelInfo levelInfo) {
        this.main = main;
        this.previewScreen = previewScreen;
        this.levelInfo = levelInfo;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");
        Label levelNameLabel = new Label(levelInfo.getName(), cloudFormSkin, "title");
        Label scoreLabel = new Label("Rollos de papel: " + levelInfo.getHighscore().getScore(), cloudFormSkin);
        Label authorLabel = new Label("Autor: " + levelInfo.getHighscore().getAuthor(), cloudFormSkin);
        TextButton startButton = new TextButton("COMENZAR", cloudFormSkin);

        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(main, Color.SKY));
        table.setFillParent(true);
        table.left().top();

        Table levelNameTable = new Table();
        levelNameTable.setBackground(new MonochromaticDrawable(main, Color.OLIVE));
        table.add(levelNameTable).pad(10).expandX().fillX().row();

        levelNameTable.pad(10);
        levelNameTable.add(levelNameLabel).expandX().left();

        Table statsTable = new Table();
        statsTable.center();
        statsTable.setBackground(new MonochromaticDrawable(main, Color.WHITE));
        table.add(statsTable).padLeft(10).padRight(10).padBottom(10).expand().fill().row();

        statsTable.add(scoreLabel).expandX().padBottom(10).row();
        statsTable.add(authorLabel).expandX().row();

        Table startButtonTable = new Table();
        table.add(startButtonTable).padLeft(10).padRight(10).padBottom(10).expandX().fillX();
        startButtonTable.add(startButton).expandX().fillX();

        stage.addActor(table);

        startButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playListener != null) playListener.playClicked();
            }
        });
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public Stage getStage() {
        return stage;
    }

    public interface PlayListener {
        void playClicked();
    }

    public void dispose() {
        stage.dispose();
    }
}
