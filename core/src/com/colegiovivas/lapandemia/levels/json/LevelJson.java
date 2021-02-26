package com.colegiovivas.lapandemia.levels.json;

import com.badlogic.gdx.utils.Array;

public class LevelJson {
    public int[] size;
    public PlayerStateJsonEntry playerState;
    public Array<WallActorJsonEntry> walls;
    public Array<FanActorJsonEntry> fans;
}
