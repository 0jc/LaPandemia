package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class StatView extends Actor {
    private final Texture icon;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    public StatView(Texture icon, BitmapFont font) {
        this.icon = icon;
        this.font = font;
        this.glyphLayout = new GlyphLayout();
        updateSize();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(icon, getX(), getY() + getHeight()/2f - icon.getHeight()/2f);
        font.draw(batch, glyphLayout,
                getX() + icon.getWidth(),
                getY() + getHeight()/2f + glyphLayout.height/2f);
    }

    protected void setText(String text) {
        glyphLayout.setText(font, " x " + text);
        updateSize();
    }

    private void updateSize() {
        setSize(icon.getWidth() + glyphLayout.width,
                Math.max(glyphLayout.height, icon.getHeight()));
    }
}
