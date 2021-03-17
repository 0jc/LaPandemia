package com.colegiovivas.lapandemia.levels.json;

/**
 * Objeto JSON para una entrada de un nivel en el catálogo de niveles.
 */
public class LevelJsonEntry {
    /**
     * Nombre del nivel de cara al usuario.
     */
    public String name;

    /**
     * ID del nivel. Lo distingue del resto internamente.
     */
    public int id;

    /**
     * Nombre del archivo en el directorio de niveles donde se especifica cómo es el mapa.
     */
    public String filename;
}
