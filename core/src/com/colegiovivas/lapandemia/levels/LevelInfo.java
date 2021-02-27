package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.files.FileHandle;

/**
 * Información acerca de un nivel disponible para jugar.
 */
public class LevelInfo {
    /**
     * ID del nivel. Se utiliza para distinguirlo de los demás niveles internamente.
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

    public LevelInfo(int id, String name, FileHandle levelJsonFile) {
        this.id = id;
        this.name = name;
        this.levelJsonFile = levelJsonFile;
        this.highscore = new Highscore(id);
    }

    /**
     * @return La ID del nivel.
     */
    public int getId() {
        return id;
    }

    /**
     * @return El nombre del nivel.
     */
    public String getName() {
        return name;
    }

    /**
     * @return El archivo JSON con la especificación del nivel.
     */
    public FileHandle getLevelJsonFile() {
        return levelJsonFile;
    }

    /**
     * @return La interfaz para leer y escribir los datos de la mejor marca del nivel.
     */
    public Highscore getHighscore() {
        return highscore;
    }
}
