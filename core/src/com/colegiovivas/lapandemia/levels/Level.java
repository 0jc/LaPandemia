package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.utils.Array;

public class Level {
    // Las dimensiones del mapa. Las coordenadas válidas son de (0, 0) a
    // (width, height). Las coordenadas inválidas no solo son inaccesibles para
    // los actores sino que además no se muestran al hacer scroll cerca de los bordes
    // del mapa.
    public int width;
    public int height;

    // Las coordenadas iniciales del PlayerActor.
    public int startX;
    public int startY;

    // La dirección inicial del PlayerActor.
    public int startXDir;
    public int startYDir;

    // Los bloques muro, que actúan como paredes en el nivel.
    public Array<Wall> walls;

    // Las hélices, que matan tanto a enemigos como al personaje.
    public Array<Fan> fans;

    public Level() {
        fans = new Array<>();
        walls = new Array<>();
    }
}
