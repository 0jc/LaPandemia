package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Actor contador de tiempo de juego.
 */
public class GameTimerLabel extends Label {
    /**
     * Segundos de juego que han transcurrido.
     */
    private float seconds;

    /**
     * Inicializa la etiqueta.
     * @param style Estilo de la etiqueta.
     */
    public GameTimerLabel(LabelStyle style) {
        super("", style);
        seconds = 0;
        updateText();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        int prevRounded = (int)Math.floor(seconds);
        seconds += delta;
        int rounded = (int)Math.floor(seconds);
        if (prevRounded != rounded) {
            updateText();
        }
    }

    /**
     * Ajusta la cantidad de segundos que se indica en el marcador.
     * @param seconds Nuevo valor.
     */
    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    /**
     * Actualiza el texto exacto que se muestra en el contador.
     */
    private void updateText() {
        int rounded = (int)Math.floor(seconds);
        setText(String.format("%02d:%02d", rounded/60, rounded % 60));
    }
}
