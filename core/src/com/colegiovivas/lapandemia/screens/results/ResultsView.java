package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;

public class ResultsView {
    private final LaPandemia main;
    private final Stage stage;
    private final Label levelNameLabel;
    private final Table levelNameRow;
    private final Label timeLabel;
    private final Table timeRow;
    private final Label paperLabel;
    private final Table paperRow;
    private final Table continueRow;

    private ContinueListener continueListener;

    public ResultsView(LaPandemia main, LevelInfo level) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 480, camera);
        stage = new Stage(viewport);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = main.assetManager.get("fonts/nice32.fnt");

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.font = main.assetManager.get("fonts/nice32.fnt");

        Table table = new Table();
        table.setFillParent(true);

        levelNameLabel = new Label(level.getName(), labelStyle);
        levelNameRow = new Table();
        levelNameRow.add(new Label("Nivel:", labelStyle)).padRight(30);
        levelNameRow.add(levelNameLabel);
        table.add(levelNameRow).row();

        timeLabel = new Label("", labelStyle);
        timeRow = new Table();
        timeRow.add(new Label("Tiempo de juego:", labelStyle)).padRight(30);
        timeRow.add(timeLabel);
        table.add(timeRow).row();

        paperLabel = new Label("", labelStyle);
        paperRow = new Table();
        paperRow.add(new Label("Rollos de papel:", labelStyle)).padRight(30);
        paperRow.add(paperLabel);
        table.add(paperRow).row();

        TextButton continueButton = new TextButton("Continuar", buttonStyle);
        continueButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                continueClicked();
                return false;
            }
        });
        continueRow = new Table();
        continueRow.add(continueButton).padTop(50);
        table.add(continueRow).row();

        timeRow.setVisible(false);
        paperRow.setVisible(false);
        continueRow.setVisible(false);

        stage.addActor(table);

        setTime(0);
        setPaperCount(0);
    }

    private void continueClicked() {
        if (continueListener != null) continueListener.continueClicked();
    }

    public void setTimeVisible(boolean visible) {
        timeRow.setVisible(visible);
    }

    public void setPaperCountVisible(boolean visible) {
        paperRow.setVisible(visible);
    }

    public void setContinueButtonVisible(boolean visible) {
        continueRow.setVisible(visible);
    }

    public void setTime(float time) {
        timeLabel.setText(String.format("%02d:%02d", (int)time/60, (int)time % 60));
    }

    public void setPaperCount(int paperCount) {
        paperLabel.setText(paperCount);
    }

    public void setContinueListener(ContinueListener continueListener) {
        this.continueListener = continueListener;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowingTime() {
        return timeRow.isVisible();
    }

    public boolean isShowingPaperCount() {
        return paperRow.isVisible();
    }

    public interface ContinueListener {
        void continueClicked();
    }
}
