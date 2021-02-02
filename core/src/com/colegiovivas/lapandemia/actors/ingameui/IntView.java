package com.colegiovivas.lapandemia.actors.ingameui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class IntView extends StatView {
    public IntView(Texture icon, BitmapFont font) {
        super(icon, font);
        setValue(0);
    }

    public void setValue(int value) {
        setText(String.valueOf(value));
    }
}
