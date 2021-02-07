package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class InvincibilityTimerLabel extends Label {
    private float timeLeft;
    private final InvincibilityTimerListener invincibilityTimerListener;

    public InvincibilityTimerLabel(Label.LabelStyle style, InvincibilityTimerListener invincibilityTimerListener) {
        super("", style);
        this.invincibilityTimerListener = invincibilityTimerListener;
        timeLeft = 0;
        valueUpdated();
    }

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

    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
        valueUpdated();
    }

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
        void newValueSet(int value);
    }
}
