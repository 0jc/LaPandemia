package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.files.FileHandle;

public class LevelInfo {
    private final int id;
    private final String name;
    private final FileHandle levelJsonFile;
    private final Highscore highscore;

    public LevelInfo(int id, String name, FileHandle levelJsonFile) {
        this.id = id;
        this.name = name;
        this.levelJsonFile = levelJsonFile;
        this.highscore = new Highscore(id);
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

    public Highscore getHighscore() {
        return highscore;
    }
}
