package com.colegiovivas.lapandemia.screens.transitions;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Clase genérica para dibujar transiciones. Ofrece la base necesaria para que sus
 * subclases puedan dibujar fácilmente diferentes tipos de transiciones, teniendo
 * en cuenta la velocidad especificada por el usuario programador que las instancia,
 * pudiendo reportar si la transición se ha completado, etc.
 */
public abstract class Transition {
    /**
     * Cámara que muestra en pantalla lo que dibuja {@link #shapeRenderer}.
     */
    private final OrthographicCamera camera;

    /**
     * Viewport de {@link #camera}.
     */
    private final Viewport viewport;

    /**
     * Tiempo en segundos que lleva completar la animación de la transición, sin tener en
     * cuenta {@link #prewait} ni {@link #postwait}.
     */
    protected final float duration;

    /**
     * Tiempo de pausa durante la ejecución de la transición antes de comenzar su animación.
     */
    protected final float prewait;

    /**
     * Tiempo de pausa durante la ejecución de la transición tras finalizarse su animación.
     */
    protected final float postwait;

    /**
     * Renderizador de figuras que las subclases deben utilizar para dibujar sus transiciones.
     * Es la clase base y no estas subclases la que gestiona sus recursos.
     */
    protected final ShapeRenderer shapeRenderer;

    /**
     * Tiempo total durante el que se lleva ejecutando la transición.
     */
    private float totalTime;

    /**
     * Inicializa los recursos necesarios para la ejecución de la transición. Una vez
     * ejecutada, su estado no se puede restablecer.
     * @param prewait Valor para {@link #prewait}.
     * @param duration Valor para {@link #duration}.
     * @param postwait Valor para {@link #postwait}.
     */
    public Transition(float prewait, float duration, float postwait) {
        this.prewait = prewait;
        this.duration = duration;
        this.postwait = postwait;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(800, 480, camera);
        shapeRenderer = new ShapeRenderer();
        totalTime = 0;

        camera.translate(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);
    }

    /**
     * Dibuja la transición tal y como se debe mostrar en el frame actual, dado el
     * porcentaje de progreso de compleción de su animación.
     * @param progress Porcentaje de progreso, entre 0 y 1.
     */
    protected abstract void draw(float progress);

    /**
     * Dibuja la transición tal y como se debe mostrar en el frame actual. Delega en
     * {@link Transition#draw(float)}.
     */
    public void draw() {
        draw(MathUtils.clamp(totalTime - prewait, 0, duration)/duration);
    }

    /**
     * Actualiza el tiempo total de ejecución de la transición. Se debe llamar una vez por
     * frame desde el momento que se quiera reproducir la transición hasta alcanzar su
     * compleción.
     * @param delta Tiempo transcurrido desde el anterior frame en segundos.
     */
    public void render(float delta) {
        totalTime += delta;
    }

    /**
     * Informa del estado de compleción de la transición.
     * @return True si la transición se ha completado o false en caso contrario.
     */
    public boolean isComplete() {
        return totalTime >= prewait + duration + postwait;
    }

    /**
     * Libera los recursos necesarios para dibujar la transición.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }

    /**
     * @return {@link #viewport}
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * @return {@link #camera}
     */
    public Camera getCamera() {
        return camera;
    }
}
