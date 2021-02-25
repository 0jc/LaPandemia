package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.ingameui.GameTimerLabel;
import com.colegiovivas.lapandemia.actors.ingameui.InvincibilityTimerLabel;

public class GameStats {
    private final Stage stage;
    private final Label masksLabel;
    private final Label paperLabel;
    private final GameTimerLabel runningTimeLabel;
    private final InvincibilityTimerLabel invincibilityTimerLabel;

    private boolean paused = false;

    public GameStats(final LaPandemia main) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 40, camera);
        stage = new Stage(viewport);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = main.assetManager.get("fonts/nice32.fnt");

        final TextureAtlas atlas = main.assetManager.get("images.pack");
        Image maskIcon = new Image(atlas.findRegion("ui-mask"));
        Image paperIcon = new Image(atlas.findRegion("ui-toiletpaper"));
        Image runningTimeIcon = new Image(atlas.findRegion("timer"));
        final Image invincibilityTimeIcon = new Image(atlas.findRegion("invincibility-timer"));

        masksLabel = new Label("0", labelStyle);
        paperLabel = new Label("0", labelStyle);
        runningTimeLabel = new GameTimerLabel(labelStyle);
        invincibilityTimerLabel = new InvincibilityTimerLabel(labelStyle, new InvincibilityTimerLabel.InvincibilityTimerListener() {
            @Override
            public void newValueSet(int value) {
                invincibilityTimeIcon.setVisible(value > 0);
            }
        });

        Table table = new Table();
        table.left();
        table.setFillParent(true);
        table.padRight(20);
        table.padLeft(20);
        table.setBackground(new BaseDrawable() {
            private final Color savedBatchColor = new Color();
            private final TextureAtlas.AtlasRegion whitePixel = atlas.findRegion("ui-whitepixel");

            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                savedBatchColor.set(batch.getColor());
                batch.setColor(0, 0, 0, 1);
                batch.draw(whitePixel, x, y, width, height);
                batch.setColor(savedBatchColor);
            }
        });

        table.add(runningTimeIcon);
        table.add(runningTimeLabel).padLeft(10).padRight(30);
        table.add(invincibilityTimeIcon);
        table.add(invincibilityTimerLabel).padLeft(10);

        Table rightElements = new Table();
        rightElements.add(maskIcon);
        rightElements.add(masksLabel).padLeft(10).padRight(30);
        rightElements.add(paperIcon);
        rightElements.add(paperLabel).padLeft(10);
        table.add(rightElements).right().expandX();

        stage.addActor(table);
    }

    public void setMaskCount(int total) {
        masksLabel.setText(Math.max(total, 0));
    }

    public void setPaperCount(int total) {
        paperLabel.setText(Math.max(total, 0));
    }

    public void setInvincibilityTime(float totalTime) {
        invincibilityTimerLabel.setTimeLeft(totalTime);
    }

    public Stage getStage() {
        return stage;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public void render(float delta) {
        if (!isPaused()) {
            runningTimeLabel.act(delta);
            invincibilityTimerLabel.act(delta);
        }
    }
}
