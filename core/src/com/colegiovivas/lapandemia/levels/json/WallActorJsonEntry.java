package com.colegiovivas.lapandemia.levels.json;

/**
 * Objeto JSON para un muro en un mapa.
 */
public class WallActorJsonEntry {
    /**
     * Patrón del sprite del muro. Se dibuja repetidas veces para cubrir toda
     * la superficie que ocupa el actor.
     */
    public String sprite;

    /**
     * Posición del muro en el mundo (x, y).
     */
    public int[] pos;

    /**
     * Dimensiones del muro (anchura, altura).
     */
    public int[] size;
}
