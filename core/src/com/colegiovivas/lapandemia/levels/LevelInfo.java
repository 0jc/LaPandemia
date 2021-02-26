package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.files.FileHandle;

public class LevelInfo {
    private final int id;
    private final String name;
    private final FileHandle levelJsonFile;

    public LevelInfo(int id, String name, FileHandle levelJsonFile) {
        this.id = id;
        this.name = name;
        this.levelJsonFile = levelJsonFile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FileHandle getLevelJsonFile() {
        return levelJsonFile;
    }
}
