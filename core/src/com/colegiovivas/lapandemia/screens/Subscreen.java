package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public abstract class Subscreen {
    protected final Rectangle screenBounds = new Rectangle();

    public void setScreenBounds(int x, int y, int width, int height) {
        screenBounds.set(x, y, width, height);
    }

    public void setScreenBounds(Rectangle bounds) {
        screenBounds.set(bounds);
    }

    public Rectangle getScreenBounds() {
        return screenBounds;
    }

    public final void draw(float delta) {
        Gdx.gl.glViewport(
                (int)screenBounds.x,
                (int)screenBounds.y,
                (int)screenBounds.width,
                (int)screenBounds.height);
        drawWithinBounds(delta);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    protected abstract void drawWithinBounds(float delta);
    public abstract void act(float delta);
    public abstract void dispose();
    public abstract void resize(int width, int height);
}
