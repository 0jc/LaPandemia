package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class VCenterInTransition extends Transition {
    public VCenterInTransition(float speed) {
        super(speed, 240);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, 800, progress);
        shapeRenderer.rect(0, 480 - progress, 800, progress);
        shapeRenderer.end();
    }
}
