package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TimeView extends StatView {
    public TimeView(Texture icon, BitmapFont font) {
        super(icon, font);
        setValue(0);
    }

    public void setValue(int seconds) {
        setText(String.format("%02d:%02d", seconds/60, seconds % 60));
    }
}
