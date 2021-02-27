package com.colegiovivas.lapandemia.gestures;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class ZoomGestureListener extends GestureDetector.GestureAdapter {
    /**
     * Razón que indica cuánto se modifica el zoom en función de cuántos píxeles se
     * separan los dedos durante el gesto pinch. Valores más grandes harán que el
     * zoom sea más brusco.
     */
    private static final float ZOOM_DISTANCE_RATIO = 0.1f / 50f;

    /**
     * Evento que se lanza cuando se perciben diferencias en el zoom.
     */
    private final ZoomListener zoomListener;

    /**
     * Si se está realizando el gesto pinch.
     */
    private boolean pinching = false;

    /**
     * Si ya no se está realizando el gesto pinch, pero todavía no se ha levantado
     * el otro dedo de la pantalla. Esto nos permite interceptar los eventos tap
     * que se producen cuando se levanta un solo dedo, de modo que
     * MovePlayerGestureListener no los reciba y por tanto no los pueda interpretar
     * erróneamente como cambios de dirección.
     */
    private boolean finishingPinch = false;

    /**
     * Última distancia entre los dedos percibida por un evento pinch. Tan solo nos
     * interesa la diferencia entre la distancia actual y la distancia previa, y SIEMPRE
     * dentro del contexto de un mismo pinch.
     */
    private float lastDistance;

    /**
     * @param zoomListener Listener al que se notificará de los eventos del zoom.
     */
    public ZoomGestureListener(ZoomListener zoomListener) {
        this.zoomListener = zoomListener;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if (!pinching) {
            pinching = true;
            finishingPinch = false;
            lastDistance = initialPointer2.dst(initialPointer1);
        }
        float distance = pointer2.dst(pointer1);
        float delta = distance - lastDistance;
        zoomListener.zoom(-delta * ZOOM_DISTANCE_RATIO);
        lastDistance = distance;
        return true;
    }

    @Override
    public void pinchStop() {
        pinching = false;
        finishingPinch = true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return pinching || finishingPinch;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (finishingPinch) {
            finishingPinch = false;
            return true;
        }
        return false;
    }

    public static interface ZoomListener {
        /**
         * Evento que indica que el zoom actual debe variar.
         * @param delta Diferencia de zoom que se debe aplicar.
         */
        void zoom(float delta);
    }
}
