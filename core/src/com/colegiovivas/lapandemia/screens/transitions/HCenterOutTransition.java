package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HCenterOutTransition extends Transition {
    public HCenterOutTransition(float speed) {
        super(speed, 400);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, 400 - progress, 480);
        shapeRenderer.rect(400 + progress, 0, 400 - progress, 480);
        shapeRenderer.end();
    }
}
