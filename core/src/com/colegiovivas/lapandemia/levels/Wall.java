package com.colegiovivas.lapandemia.levels;

public class Wall {
    public int x;
    public int y;
    public int w;
    public int h;

    public Wall(int x, int y, int w, int h, int factor) {
        this(x*factor, y*factor, w*factor, h*factor);
    }

    public Wall(int x, int y, int w, int h) {
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
