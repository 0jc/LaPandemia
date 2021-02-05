package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class StatsSubscreen extends Subscreen {
    private final Stage stage;
    private final Label masksLabel;
    private final Label paperLabel;

    public StatsSubscreen(final LaPandemia parent) {
        Camera uiCamera = new OrthographicCamera();
        Viewport uiViewport = new StretchViewport(800, 40, uiCamera);
        stage = new Stage(uiViewport);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = parent.assetManager.get("fonts/nice32.fnt");

        masksLabel = new Label("0", labelStyle);
        paperLabel = new Label("0", labelStyle);

        final TextureAtlas atlas = parent.assetManager.get("images.pack");
        Table table = new Table();
        table.add(new Image(atlas.findRegion("ui-mask")));
        table.add(masksLabel).center().padLeft(10).padRight(30);
        table.add(new Image(atlas.findRegion("ui-toiletpaper")));
        table.add(paperLabel).center().padLeft(10);
        table.right();
        table.setFillParent(true);
        table.padRight(20);
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
        stage.addActor(table);
    }

    public void setMaskCount(int total) {
        masksLabel.setText(Math.max(total, 0));
    }

    public void setPaperCount(int total) {
        paperLabel.setText(Math.max(total, 0));
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
}
