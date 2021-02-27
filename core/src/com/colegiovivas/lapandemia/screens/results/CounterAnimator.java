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
    private float totalTime;

    /**
     * Valor inicial del contador.
     */
    private float startValue;

    /**
     * Valor final del contador.
     */
    private float endValue;

    /**
     * Tiempo actual que se lleva reproduciendo la animación.
     */
    private float currentTime;

    public void init(float totalTime, float startValue, float endValue) {
        this.totalTime = totalTime;
        this.startValue = startValue;
        this.endValue = endValue;
        currentTime = 0;
    }

    /**
     * Actualiza currentTime y reporta el valor actual del contador.
     * @param delta Tiempo en segundos que ha transcurrido desde la última llamada.
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
