package com.colegiovivas.lapandemia.levels.json;

/**
 * Objeto JSON para un ventilador en un mapa.
 */
public class FanActorJsonEntry {
    /**
     * Sprite del ventilador.
     */
    public String sprite;

    /**
     * Duración en segundos de cada frame de {@link #sprite}.
     */
    public float frameDuration;

    /**
     * Posición del ventilador en el mundo (x, y).
     */
    public int[] pos;
}
