package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición de dos rectángulos separándose desde el centro sobre el eje horizontal.
 */
public class HCenterInTransition extends Transition {
    public HCenterInTransition(float prewait, float duration, float postwait) {
        super(prewait, duration, postwait);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, 400*progress, 480);
        shapeRenderer.rect(800 - 400*progress, 0, 400*progress, 480);
        shapeRenderer.end();
    }
}
