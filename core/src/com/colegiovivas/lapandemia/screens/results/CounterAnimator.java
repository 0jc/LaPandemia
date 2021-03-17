package com.colegiovivas.lapandemia.screens.results;

/**
 * Dispositivo que reporta el valor que se debe mostrar en cada instante para
 * provocar la animación en la que un valor numérico recorre los valores de
 * un intervalo específico de principio a fin en un tiempo total exacto
 * establecido.
 */
class CounterAnimator {
    /**
     * Tiempo total de recorrido.
     */
    private final float totalTime;

    /**
     * Valor inicial del contador.
     */
    private final float startValue;

    /**
     * Valor final del contador.
     */
    private final float endValue;

    /**
     * Tiempo actual que se lleva reproduciendo la animación.
     */
    private float currentTime;

    /**
     * Inicializa el contador.
     * @param totalTime Valor para {@link #totalTime}.
     * @param startValue Valor para {@link #startValue}.
     * @param endValue Valor para {@link #endValue}.
     */
    public CounterAnimator(float totalTime, float startValue, float endValue) {
        this.totalTime = totalTime;
        this.startValue = startValue;
        this.endValue = endValue;
        currentTime = 0;
    }

    /**
     * Actualiza {@link #currentTime} y reporta el valor actual del contador.
     * @param delta Tiempo en segundos que ha transcurrido desde la última actualización.
     * @return El valor actual del contador.
     */
    public float update(float delta) {
        currentTime = Math.min(currentTime + delta, totalTime);
        return startValue + (endValue - startValue) * (currentTime / totalTime);
    }

    /**
     * @return True si y solo si el valor todavía no ha alcanzado su valor máximo.
     */
    public boolean isIncreasing() {
        return currentTime < totalTime;
    }
}
