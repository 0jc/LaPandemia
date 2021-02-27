package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición que revela la pantalla gradualmente en sentido horizontal
 * de izquierda a derecha.
 */
public class LeftOutTransition extends Transition {
    public LeftOutTransition(float duration) {
        super(duration);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(800*progress, 0, 800 - 800*progress, 480);
        shapeRenderer.end();
    }
}
