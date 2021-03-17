package com.colegiovivas.lapandemia.levels.json;

import com.badlogic.gdx.utils.Array;

/**
 * Objeto JSON para el mapa de un nivel.
 */
public class LevelJson {
    /**
     * Tamaño del mapa (anchura, altura).
     */
    public int[] size;

    /**
     * Estado en el que se encuentra el personaje inicialmente.
     */
    public PlayerStateJsonEntry playerState;

    /**
     * Muros del nivel.
     */
    public Array<WallActorJsonEntry> walls;

    /**
     * Ventiladores del nivel.
     */
    public Array<FanActorJsonEntry> fans;
}
