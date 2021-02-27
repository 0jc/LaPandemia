package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Highscore {
    private final Preferences levelPreferences;

    public Highscore(int levelId) {
        levelPreferences = Gdx.app.getPreferences("highscore-" + levelId);
    }

    public int getScore() {
        return levelPreferences.getInteger("score", 0);
    }

    public String getAuthor() {
        return levelPreferences.getString("author");
    }

    public void set(int score, String author) {
        levelPreferences.putInteger("score", score);
        levelPreferences.putString("author", author);
        levelPreferences.flush();
    }
}
