package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RectanglesTransition extends Subscreen {
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final float baseLine;
    private final float biggerRectLength;
    private final float speed;
    private final boolean vertical;
    private final Dir dir;

    private float progress;

    public RectanglesTransition(float baseLine, Dir dir, boolean vertical, float speed){
        this.baseLine = baseLine;
        this.dir = dir;
        this.speed = speed;
        this.vertical = vertical;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(800, 480, camera);
        float totalLength = vertical ? viewport.getWorldHeight() : viewport.getWorldWidth();
        biggerRectLength = Math.max(baseLine, totalLength - baseLine);
        shapeRenderer = new ShapeRenderer();

        camera.translate(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
        progress = -1;
    }

    public void start() {
        progress = 0;
    }

    public void stop() {
        progress = -1;
    }

    public boolean isPlaying() {
        return progress >= 0;
    }

    @Override
    protected void drawWithinBounds(float delta) {
        if (isPlaying()) {
            float firstX = 0;
            float firstY = 0;
            float firstW = vertical ? viewport.getWorldWidth() : dir == Dir.IN ? progress : baseLine - progress;
            float firstH = !vertical ? viewport.getWorldHeight() : dir == Dir.IN ? progress : baseLine - progress;
            float secondX = vertical ? 0 : dir == Dir.IN ? viewport.getWorldWidth() - progress : baseLine + progress;
            float secondY = !vertical ? 0 : dir == Dir.IN ? viewport.getWorldHeight() - progress : baseLine + progress;
            float secondW = vertical ? viewport.getWorldWidth() : viewport.getWorldWidth() - secondX;
            float secondH = !vertical ? viewport.getWorldHeight() : viewport.getWorldHeight() - secondY;

            camera.update();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 1);
            if (firstW >= 0 && firstH >= 0) {
                shapeRenderer.rect(firstX, firstY, firstW, firstH);
            }
            if (secondW >= 0 && secondH >= 0) {
                shapeRenderer.rect(secondX, secondY, secondW, secondH);
            }
            shapeRenderer.end();
        }
    }

    @Override
    public void act(float delta) {
        if (isPlaying()) {
            progress = Math.min(progress + speed*delta, biggerRectLength);
            if (progress == biggerRectLength) {
                stop();
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public enum Dir { IN, OUT }
}
