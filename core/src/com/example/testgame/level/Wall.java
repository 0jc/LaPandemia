package com.example.testgame.level;

public class Wall {
    public float x;
    public float y;
    public float w;
    public float h;

    public Wall(float x, float y, float w, float h) {
        // Ya que el bitmap patrón es de 32x32.
        if (w % 32 != 0 || h % 32 != 0) {
            throw new IllegalArgumentException();
        }

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
