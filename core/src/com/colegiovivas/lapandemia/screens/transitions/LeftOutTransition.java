package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LeftOutTransition extends Transition {
    public LeftOutTransition(float speed) {
        super(speed, 800);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(progress, 0, 800 - progress, 480);
        shapeRenderer.end();
    }
}
