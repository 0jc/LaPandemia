package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.VirusActor;
import com.colegiovivas.lapandemia.screens.GameScreen;

public class VirusPool extends Pool<VirusActor> {
    private final LaPandemia game;

    public VirusPool(final LaPandemia game) {
        this.game = game;
    }

    @Override
    protected VirusActor newObject() {
        return new VirusActor(game);
    }
}
