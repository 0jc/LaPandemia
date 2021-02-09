package com.colegiovivas.lapandemia.level;

import com.badlogic.gdx.utils.Array;

public class Level {
    public int[] size;
    public LevelPlayerState playerState;
    public Array<LevelWallActor> walls;
    public Array<LevelFanActor> fans;
}
