package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición de dos rectángulos separándose desde el centro sobre el eje horizontal.
 */
public class HCenterOutTransition extends Transition {
    public HCenterOutTransition(float duration) {
        super(duration);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, 400 - 400*progress, 480);
        shapeRenderer.rect(400 + 400*progress, 0, 400 - 400*progress, 480);
        shapeRenderer.end();
    }
}
