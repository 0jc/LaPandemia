package com.colegiovivas.lapandemia.gestures;

import com.badlogic.gdx.input.GestureDetector;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;

/**
 * Procesador de entrada de datos para cambios de dirección de {@link PlayerActor}.
 */
public class MovePlayerGestureListener extends GestureDetector.GestureAdapter {
    /**
     * Si se está realizando el gesto pan.
     */
    private boolean panning = false;

    /**
     * Personaje al que se reportan los eventos.
     */
    private final PlayerActor playerActor;

    /**
     * Inicializa el procesador.
     * @param playerActor Actor personaje que será notificado de los cambios de dirección.
     */
    public MovePlayerGestureListener(final PlayerActor playerActor) {
        this.playerActor = playerActor;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (!panning) {
            panning = true;
            boolean horizontal = Math.abs(deltaX) > Math.abs(deltaY);
            int xDir = !horizontal ? 0 : (deltaX > 0 ? 1 : -1);
            int yDir = horizontal ? 0 : (deltaY > 0 ? -1 : 1);
            playerActor.turn(xDir, yDir);
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        panning = false;
        return true;
    }
}
