package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición que cubre gradualmente la pantalla en dirección horizontal en
 * sentido de derecha a izquierda.
 */
public class LeftInTransition extends Transition {
    public LeftInTransition(float duration) {
        super(duration);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(800 - 800*progress, 0, 800*progress, 480);
        shapeRenderer.end();
    }
}
