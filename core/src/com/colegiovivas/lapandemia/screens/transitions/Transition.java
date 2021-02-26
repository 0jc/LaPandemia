package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Transition {
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final float speed;
    private final float maxProgress;

    protected final ShapeRenderer shapeRenderer;

    private float progress;
    private float totalTime;
    private boolean playing = false;

    public Transition(float speed, float maxProgress) {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(800, 480, camera);
        shapeRenderer = new ShapeRenderer();
        this.speed = speed;
        this.maxProgress = maxProgress;

        camera.translate(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
    }

    protected abstract void draw(float progress);

    public void draw() {
        if (playing) {
            draw(progress);
        }
    }

    public void render(float delta) {
        if (playing) {
            totalTime += delta;
            progress = Math.min(maxProgress, totalTime*speed);
        }
    }

    public boolean isComplete() {
        return progress == maxProgress;
    }

    public void start() {
        totalTime = 0;
        progress = 0;
        playing = true;
    }

    public void stop() {
        playing = false;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Camera getCamera() {
        return camera;
    }
}
