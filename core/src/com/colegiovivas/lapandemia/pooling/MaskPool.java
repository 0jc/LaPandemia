package com.colegiovivas.lapandemia.pooling;

import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.MaskActor;
import com.colegiovivas.lapandemia.gameplay.VirusActor;

public class MaskPool extends Pool<MaskActor> {
    private final LaPandemia game;

    public MaskPool(final LaPandemia game) {
        this.game = game;
    }

    @Override
    protected MaskActor newObject() {
        return new MaskActor(game);
    }
}
