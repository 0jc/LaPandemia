package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Clase genérica para dibujar transiciones. Ofrece la base necesaria para que sus
 * subclases puedan dibujar fácilmente diferentes tipos de transiciones, teniendo
 * en cuenta la velocidad especificada por el usuario programador que las instancia,
 * pudiendo reportar si la transición se ha completado, etc.
 */
public abstract class Transition {
    private final OrthographicCamera camera;
    private final Viewport viewport;
    /**
     * Tiempo en segundos que lleva completar la transición.
     */
    private final float duration;

    /**
     * Renderizador de figuras disponible para las subclases y cuyos recursos se
     * gestionan desde la base.
     */
    protected final ShapeRenderer shapeRenderer;

    /**
     * Tiempo total actual de ejecución de la transición.
     */
    private float totalTime;

    /**
     * True si la transición se está reproduciendo o false en caso contrario.
     */
    private boolean playing = false;

    public Transition(float duration) {
        this.duration = duration;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(800, 480, camera);
        shapeRenderer = new ShapeRenderer();

        camera.translate(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
    }

    /**
     * Dibuja la transición tal y como se debe mostrar en el frame actual, dado el
     * porcentaje de progreso para su compleción.
     * @param progress Porcentaje de progreso, entre 0 y 1.
     */
    protected abstract void draw(float progress);

    public void draw() {
        if (playing) {
            draw(totalTime/duration);
        }
    }

    /**
     * Actualiza el tiempo total de ejecución de la transición si esta se está
     * reproduciendo. Se debe llamar una vez por frame.
     * @param delta Tiempo transcurrido desde el anterior frame.
     */
    public void render(float delta) {
        if (playing) {
            totalTime = Math.min(duration, totalTime + delta);
        }
    }

    /**
     * Devuelve true si la transición se ha completado o false en caso contrario.
     * @return
     */
    public boolean isComplete() {
        return duration == totalTime;
    }

    /**
     * Resetea el estado de la transición y comienza su reproducción.
     */
    public void start() {
        totalTime = 0;
        playing = true;
    }

    /**
     * Frena la reproducción de la transición.
     */
    public void stop() {
        playing = false;
    }

    /**
     * Libera los recursos necesarios para dibujar la transición.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Camera getCamera() {
        return camera;
    }
}
