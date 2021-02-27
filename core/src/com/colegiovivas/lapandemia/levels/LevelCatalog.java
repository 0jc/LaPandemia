package com.colegiovivas.lapandemia.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.colegiovivas.lapandemia.levels.json.LevelJsonEntry;
import com.colegiovivas.lapandemia.levels.json.LevelsJson;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Catálogo de niveles. Proporciona información sobre todos los niveles existentes en el juego.
 */
public class LevelCatalog {
    /**
     * Información sobre cada nivel disponible.
     */
    private final Array<LevelInfo> catalog;

    public LevelCatalog() {
        catalog = new Array<>();

        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(false);
        json.setOutputType(JsonWriter.OutputType.json);

        LevelsJson levelsJson = json.fromJson(LevelsJson.class, Gdx.files.internal("levels/levels.json"));
        for (LevelJsonEntry levelJsonEntry : levelsJson.levels) {
            catalog.add(new LevelInfo(
                    levelJsonEntry.id,
                    levelJsonEntry.name,
                    Gdx.files.internal("levels/" + levelJsonEntry.filename)));
        }
    }

    /**
     * @return Iterable sobre los LevelInfo de cada nivel.
     */
    public Iterable<LevelInfo> levels() {
        return new CatalogIterable();
    }

    /**
     * Iterable sobre los LevelInfo de los niveles existentes.
     */
    private class CatalogIterable implements Iterable<LevelInfo> {
        @Override
        @NotNull
        public CatalogIterator iterator() {
            return new CatalogIterator();
        }

        private class CatalogIterator implements Iterator<LevelInfo> {
            private int i = -1;

            @Override
            public boolean hasNext() {
                return i + 1 < catalog.size;
            }

            @Override
            public LevelInfo next() {
                i++;
                return catalog.get(i);
            }

            @Override
            public void remove() {
                catalog.removeIndex(i);
            }
        }
    }
}
