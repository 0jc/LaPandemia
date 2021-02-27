package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.colegiovivas.lapandemia.LaPandemia;

public class MonochromaticDrawable extends BaseDrawable {
    private final LaPandemia main;
    private final TextureAtlas.AtlasRegion whitePixel;
    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public MonochromaticDrawable(LaPandemia main, Color color) {
        this.main = main;
        whitePixel = ((TextureAtlas)main.assetManager.get("images.pack")).findRegion("ui-whitepixel");
        this.color = color;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color savedBatchColor = main.colorPool.obtain();
        try {
            savedBatchColor.set(batch.getColor());
            batch.setColor(color);
            batch.draw(whitePixel, x, y, width, height);
            batch.setColor(savedBatchColor);
        } finally {
            main.colorPool.free(savedBatchColor);
        }
    }
}
