package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Actor contador de tiempo de invencibilidad.
 */
public class InvincibilityTimerLabel extends Label {
    /**
     * Tiempo restante para que se termine la invencibilidad. Mientras que el contador gráfico
     * termina en 1, este lo hace en 0.
     */
    private float timeLeft;

    /**
     * Listener al que se reportan cambios en el valor mostrado en el contador.
     * Útil para que el icono que se muestra junto a este actor sepa cuándo
     * aparecer y desaparecer.
     */
    private final InvincibilityTimerListener invincibilityTimerListener;

    public InvincibilityTimerLabel(Label.LabelStyle style, InvincibilityTimerListener invincibilityTimerListener) {
        super("", style);
        this.invincibilityTimerListener = invincibilityTimerListener;
        timeLeft = 0;
        valueUpdated();
    }

    /**
     * Actualiza el estado del contador.
     * @param delta Segundos transcurridos desde la última actualización.
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        int prevRounded = (int)Math.ceil(timeLeft);
        timeLeft = Math.max(0, timeLeft - delta);
        int rounded = (int)Math.ceil(timeLeft);
        if (prevRounded != rounded) {
            valueUpdated();
        }
    }

    /**
     * Establece el tiempo restante para que se termine el efecto de invencibilidad.
     * @param timeLeft
     */
    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
        valueUpdated();
    }

    /**
     * Actualiza el valor mostrado gráficamente.
     */
    private void valueUpdated() {
        int rounded = (int)Math.ceil(timeLeft);
        if (rounded == 0) {
            setText("");
        } else {
            setText(rounded);
        }

        invincibilityTimerListener.newValueSet(rounded);
    }

    public interface InvincibilityTimerListener {
        /**
         * Reporta de que el valor que se muestra en el contador ha cambiado.
         * @param value Tiempo restante en segundos.
         */
        void newValueSet(int value);
    }
}
