package com.colegiovivas.lapandemia.levels.json;

/**
 * Objeto JSON para los datos del estado en el que se encuentra el personaje en
 * el inicio de la partida.
 */
public class PlayerStateJsonEntry {
    /**
     * Posición inicial del personaje en el mundo (x, y).
     */
    public int[] pos;

    /**
     * Dirección inicial del personaje (xDir, yDir).
     *
     * EJE X: xDir == -1 =&gt; izquierda, xDir == 1 =&gt; derecha, xDir == 0 =&gt; constante.
     * EJE Y: yDir == -1 =&gt; abajo, yDir == 1 =&gt; arriba, yDir == 0 =&gt; constante.
     */
    public int[] dir;
}
