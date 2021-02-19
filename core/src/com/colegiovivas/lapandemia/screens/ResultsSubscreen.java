package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

public class ResultsSubscreen extends Subscreen{
    private final LaPandemia game;
    private final Stage stage;
    private final Label levelIdLabel;
    private final Table levelIdRow;
    private final Label timeLabel;
    private final Table timeRow;
    private final Label paperLabel;
    private final Table paperRow;
    private final Table continueRow;
    private boolean accepted = false;

    public ResultsSubscreen(LaPandemia game, int levelId) {
        this.game = game;

        Camera uiCamera = new OrthographicCamera();
        Viewport uiViewport = new StretchViewport(800, 480, uiCamera);
        stage = new Stage(uiViewport);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = game.assetManager.get("fonts/nice32.fnt");

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.font = game.assetManager.get("fonts/nice32.fnt");

        Table table = new Table();
        table.setFillParent(true);

        levelIdLabel = new Label(levelId + "", labelStyle);
        levelIdRow = new Table();
        levelIdRow.add(new Label("ID del nivel:", labelStyle)).padRight(30);
        levelIdRow.add(levelIdLabel);
        table.add(levelIdRow).row();

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
                ResultsSubscreen.this.accepted = true;
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
    }

    @Override
    protected void drawWithinBounds(float delta) {
        stage.draw();
    }

    @Override
    public void act(float delta) {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public boolean showingTime() {
        return timeRow.isVisible();
    }

    public boolean showingPaperCount() {
        return paperRow.isVisible();
    }

    public boolean showingContinueButton() {
        return continueRow.isVisible();
    }

    public void showTime() {
        timeRow.setVisible(true);
    }

    public void showPaperCount() {
        paperRow.setVisible(true);
    }

    public void showContinueButton() {
        continueRow.setVisible(true);
    }

    public void setTime(float time) {
        timeLabel.setText(String.format("%02d:%02d", (int)time/60, (int)time % 60));
    }

    public void setPaperCount(int paperCount) {
        paperLabel.setText(paperCount);
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
