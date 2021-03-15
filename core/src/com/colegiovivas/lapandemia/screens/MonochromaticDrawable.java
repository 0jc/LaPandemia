package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

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
     * Píxel blanco que se necesita para dibujar del color especificado.
     */
    private final TextureRegion whitePixel;

    /**
     * Color con el que el Drawable dibuja.
     */
    private Color newColor;

    /**
     * @param newColor El nuevo valor para {@link #newColor}.
     */
    public void setNewColor(Color newColor) {
        this.newColor = newColor;
    }

    /**
     * Prepara los recursos necesarios para poder dibujar.
     * @param whitePixel El valor para {@link #whitePixel}.
     * @param newColor El valor para {@link #newColor}.
     */
    public MonochromaticDrawable(TextureRegion whitePixel, Color newColor) {
        this.whitePixel = whitePixel;
        this.newColor = newColor;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        Color batchColor = batch.getColor();
        batch.setColor(newColor);
        batch.draw(whitePixel, x, y, width, height);
        batch.setColor(batchColor);
    }
}
