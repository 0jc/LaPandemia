package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición que cubre gradualmente la pantalla en dirección vertical en
 * sentido de arriba a abajo.
 */
public class TopInTransition extends Transition {
    /**
     * Inicializa la transición.
     * @param prewait Valor para {@link Transition#prewait}.
     * @param duration Valor para {@link Transition#duration}.
     * @param postwait Valor para {@link Transition#postwait}.
     */
    public TopInTransition(float prewait, float duration, float postwait) {
        super(prewait, duration, postwait);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 480 - 480*progress, 800, 480*progress);
        shapeRenderer.end();
    }
}
