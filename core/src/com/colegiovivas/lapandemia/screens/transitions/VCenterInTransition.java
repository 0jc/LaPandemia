package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Transición de dos rectángulos acercándose hacia el centro por el eje vertical.
 */
public class VCenterInTransition extends Transition {
    /**
     * Inicializa la transición.
     * @param prewait Valor para {@link Transition#prewait}.
     * @param duration Valor para {@link Transition#duration}.
     * @param postwait Valor para {@link Transition#postwait}.
     */
    public VCenterInTransition(float prewait, float duration, float postwait) {
        super(prewait, duration, postwait);
    }

    @Override
    protected void draw(float progress) {
        getCamera().update();
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, 800, 240*progress);
        shapeRenderer.rect(0, 480 - 240*progress, 800, 240*progress);
        shapeRenderer.end();
    }
}
