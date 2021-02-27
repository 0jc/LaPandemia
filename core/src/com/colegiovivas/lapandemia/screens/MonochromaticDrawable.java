package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.colegiovivas.lapandemia.LaPandemia;

/**
 * Drawable de Libgdx que dibuja en un único color. Útil para pintar
 * fácilmente interfaces de usuario mediante tablas, entre otras cosas
 * si solo se desea plantear una interfaz simple y funcional sin tener
 * que recurrir a skins para todos los elementos de la pantalla.
 *
 * La técnica en la que se inspira viene explicada en varios hilos en
 * StackOverflow y requiere tener a mano una imagen en blanco. Curiosamente
 * complicado si se tiene en cuenta lo básica que es la funcionalidad
 * que esta clase cumple realmente...
 */
public class MonochromaticDrawable extends BaseDrawable {
    /**
     * Clase principal, para poder obtener el píxel blanco.
     */
    private final LaPandemia main;

    private final TextureAtlas.AtlasRegion whitePixel;

    /**
     * Color con el que el Drawable dibuja.
     */
    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public MonochromaticDrawable(LaPandemia main, Color color) {
        this.main = main;
        whitePixel = ((TextureAtlas)main.assetManager.get("images.pack")).findRegion("ui-whitepixel");
        this.color = color;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color savedBatchColor = main.colorPool.obtain();
        try {
            savedBatchColor.set(batch.getColor());
            batch.setColor(color);
            batch.draw(whitePixel, x, y, width, height);
            batch.setColor(savedBatchColor);
        } finally {
            main.colorPool.free(savedBatchColor);
        }
    }
}
