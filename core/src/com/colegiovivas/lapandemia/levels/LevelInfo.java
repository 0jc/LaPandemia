package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.files.FileHandle;

/**
 * Información acerca de un nivel disponible para jugar.
 */
public class LevelInfo {
    /**
     * ID del nivel. Se utiliza para distinguirlo internamente del resto.
     */
    private final int id;

    /**
     * Nombre del nivel. Es el que se utiliza de cara al usuario.
     */
    private final String name;

    /**
     * Archivo JSON con la especificación del mapa.
     */
    private final FileHandle levelJsonFile;

    /**
     * Interfaz para leer y escribir los datos de la mejor marca en el nivel.
     */
    private final Highscore highscore;

    /**
     * Establece y carga información sobre un mapa del juego.
     * @param id Valor para {@link #id}.
     * @param name Valor para {@link #name}.
     * @param levelJsonFile Valor para {@link #levelJsonFile}.
     */
    public LevelInfo(int id, String name, FileHandle levelJsonFile) {
        this.id = id;
        this.name = name;
        this.levelJsonFile = levelJsonFile;
        this.highscore = new Highscore(id);
    }

    /**
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * @return {@link #levelJsonFile}
     */
    public FileHandle getLevelJsonFile() {
        return levelJsonFile;
    }

    /**
     * @return {@link #highscore}
     */
    public Highscore getHighscore() {
        return highscore;
    }
}
