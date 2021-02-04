package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;

public abstract class Subscreen {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public void setScreenBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public final void draw(float delta) {
        Gdx.gl.glViewport(x, y, width, height);
        drawWithinBounds(delta);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    protected abstract void drawWithinBounds(float delta);
    public abstract void act(float delta);
    public abstract void dispose();
    public abstract void resize(int width, int height);
}
