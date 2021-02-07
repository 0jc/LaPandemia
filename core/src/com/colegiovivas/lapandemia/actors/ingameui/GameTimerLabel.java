package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameTimerLabel extends Label {
    private float seconds;

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

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    private void updateText() {
        int rounded = (int)Math.floor(seconds);
        setText(String.format("%02d:%02d", rounded/60, rounded % 60));
    }
}
