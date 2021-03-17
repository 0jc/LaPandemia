package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición que revela la pantalla gradualmente en sentido horizontal
 * de izquierda a derecha.
 */
public class LeftOutTransition extends Transition {
    /**
     * Inicializa la transición.
     * @param prewait Valor para {@link Transition#prewait}.
     * @param duration Valor para {@link Transition#duration}.
     * @param postwait Valor para {@link Transition#postwait}.
     */
    public LeftOutTransition(float prewait, float duration, float postwait) {
        super(prewait, duration, postwait);
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
