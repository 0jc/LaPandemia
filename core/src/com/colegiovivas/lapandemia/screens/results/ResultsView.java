package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

public class ResultsView {
    private final LaPandemia main;
    private final Stage stage;
    private final Label levelNameLabel;
    private final Label timeLeftLabel;
    private final Label timeRightLabel;
    private final Label paperLeftLabel;
    private final Label paperRightLabel;
    private final Label nickLabel;
    private final TextField nickField;
    private final TextButton continueButton;

    private ContinueListener continueListener;

    public ResultsView(LaPandemia main, LevelInfo level) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");

        levelNameLabel = new Label(level.getName(), cloudFormSkin, "title");
        timeLeftLabel = new Label("Tiempo de juego:", cloudFormSkin);
        timeRightLabel = new Label("", cloudFormSkin);
        paperLeftLabel = new Label("Rollos de papel:", cloudFormSkin);
        paperRightLabel = new Label("", cloudFormSkin);
        continueButton = new TextButton("Volver", cloudFormSkin);
        nickLabel = new Label("Tu nombre:", cloudFormSkin);
        nickField = new TextField("", cloudFormSkin);
        nickField.setText("Profesor Bacterio");

        boolean debug = false;

        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(main, Color.CYAN));
        table.setFillParent(true);
        table.setDebug(debug);
        table.left().top();

        Table levelNameTable = new Table();
        levelNameTable.setBackground(new MonochromaticDrawable(main, Color.MAROON));
        levelNameTable.setDebug(debug);
        table.add(levelNameTable).pad(10).expandX().fillX().row();

        levelNameTable.pad(10);
        levelNameTable.add(levelNameLabel).expandX().left();

        Table statsTable = new Table();
        statsTable.setBackground(new MonochromaticDrawable(main, Color.WHITE));
        statsTable.setDebug(debug);
        table.add(statsTable).padLeft(10).padRight(10).padBottom(10).expand().fill().row();

        statsTable.padLeft(10).padRight(10).padTop(10).padBottom(10).top();
        statsTable.add(timeLeftLabel).expandX().padRight(15).right();
        statsTable.add(timeRightLabel).expandX().left().row();

        statsTable.add(paperLeftLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(paperRightLabel).expandX().left().padTop(10).row();

        statsTable.add(nickLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(nickField).expandX().left().padTop(10).row();

        table.add(continueButton).expandX().fillX().padLeft(10).padRight(10).padBottom(10).row();

        continueButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                continueClicked();
            }
        });

        stage.addActor(table);

        setTime(0);
        setPaperCount(0);

        setContinueButtonVisible(false);
        setNicknameVisible(false);
        setPaperCountVisible(false);
        setTimeVisible(false);
    }

    private void continueClicked() {
        if (continueListener != null) continueListener.continueClicked();
    }

    public void setTitleColor(Color color) {
        ((MonochromaticDrawable)((Table)levelNameLabel.getParent()).getBackground()).setColor(color);
    }

    public void setTimeVisible(boolean visible) {
        timeLeftLabel.setVisible(visible);
        timeRightLabel.setVisible(visible);
    }

    public void setPaperCountVisible(boolean visible) {
        paperLeftLabel.setVisible(visible);
        paperRightLabel.setVisible(visible);
    }

    public void setNicknameVisible(boolean visible) {
        nickLabel.setVisible(visible);
        nickField.setVisible(visible);
    }

    public void setContinueButtonVisible(boolean visible) {
        continueButton.setVisible(visible);
    }

    public void setContinueButtonText(String text) {
        continueButton.setText(text);
    }

    public void setTime(float time) {
        timeRightLabel.setText(String.format("%02d:%02d", (int)time/60, (int)time % 60));
    }

    public void setPaperCount(int paperCount) {
        paperRightLabel.setText(paperCount);
    }

    public void setContinueListener(ContinueListener continueListener) {
        this.continueListener = continueListener;
    }

    public Stage getStage() {
        return stage;
    }

    public String getNickname() {
        return nickField.getText();
    }

    public boolean isShowingTime() {
        return timeRightLabel.isVisible();
    }

    public boolean isShowingPaperCount() {
        return paperRightLabel.isVisible();
    }

    public interface ContinueListener {
        void continueClicked();
    }
}
