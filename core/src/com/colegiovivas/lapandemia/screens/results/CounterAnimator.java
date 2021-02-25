package com.colegiovivas.lapandemia.screens.results;

class CounterAnimator {
    private float totalTime;
    private float startValue;
    private float endValue;
    private float currentTime;

    public void init(float totalTime, float startValue, float endValue) {
        this.totalTime = totalTime;
        this.startValue = startValue;
        this.endValue = endValue;
        currentTime = 0;
    }

    public float update(float delta) {
        currentTime = Math.min(currentTime + delta, totalTime);
        return startValue + (endValue - startValue) * (currentTime / totalTime);
    }

    public boolean isIncreasing() {
        return currentTime < totalTime;
    }

    public float getStartValue() {
        return startValue;
    }
}
