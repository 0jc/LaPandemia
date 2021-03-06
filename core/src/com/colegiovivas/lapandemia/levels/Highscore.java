package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Interfaz hacia los datos de la puntuación más alta de un nivel.
 */
public class Highscore {
    /**
     * Preferencias compartidas donde se guardan los datos de la puntuación más alta.
     */
    private final Preferences levelPreferences;

    /**
     * @param levelId ID del nivel con cuyos datos se trabajará.
     */
    public Highscore(int levelId) {
        levelPreferences = Gdx.app.getPreferences("highscore-" + levelId);
    }

    /**
     * @return Puntuación más alta en el nivel.
     */
    public int getScore() {
        return levelPreferences.getInteger("score", 0);
    }

    /**
     * @return Autor del récord.
     */
    public String getAuthor() {
        return levelPreferences.getString("author", "Nadie");
    }

    /**
     * Establece un nuevo récord.
     * @param score Puntuación para guardar.
     * @param author Nombre indicado por el jugador para guardar junto a la puntuación.
     */
    public void set(int score, String author) {
        levelPreferences.putInteger("score", score);
        levelPreferences.putString("author", author);
        levelPreferences.flush();
    }
}
