package com.colegiovivas.lapandemia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.levels.LevelCatalog;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.main.MainScreen;
import com.colegiovivas.lapandemia.screens.results.ResultsScreen;
import com.colegiovivas.lapandemia.screens.game.GameScreen;
import com.colegiovivas.lapandemia.screens.LoadingScreen;

/**
 * La clase principal del juego. Coordina la gestión de las pantallas y ofrece
 * acceso a varios elementos de uso global en la aplicación, como por ejemplo
 * los fondos de diversas clases, que permiten reciclar instancias liberadas
 * o preparadas de objetos de una clase determinada en lugar de tener que
 * instanciar otros nuevos para reducir la actividad del recolector de basura
 * y mejorar así el rendimiento del juego.
 */
public class LaPandemia extends Game {
    /**
     * El gestor de assets (recursos como imágenes y música). Permite cargar una
     * única vez dichos recursos, acceder a ellos fácilmente cuando es necesario
     * y luego finalmente liberarlos.
     */
    public AssetManager assetManager;

    /**
     * Fondo global para instancias de Rectangle.
     */
    public Pool<Rectangle> rectPool;

    /**
     * Fondo global para instancias de Array&lt;Actor&gt;.
     */
    public Pool<Array<Actor>> actorArrayPool;

    /**
     * Fondo global para instancias de Color.
     */
    public Pool<Color> colorPool;

    /**
     * La pantalla a la que saltar después de cargar los recursos. Si es null, se
     * entiende que la aplicación se acaba de iniciar y se carga el menú principal.
     * Al producirse ciertos eventos como la pausa de la aplicación, se establece
     * el valor de esta propiedad al de la pantalla actual para poder restaurarla
     * después tras la carga de recursos.
     */
    private Screen nextScreen = null;

    /**
     * Interfaz hacia el catálogo de niveles disponibles.
     */
    private LevelCatalog levelCatalog;

    @Override
    public void create() {
        Gdx.app.log("LaPandemia", "create()");

        levelCatalog = new LevelCatalog();
        assetManager = new AssetManager();

        abstract class PoolableRectangle extends Rectangle implements Pool.Poolable {}
        rectPool = new Pool<Rectangle>() {
            @Override
            protected PoolableRectangle newObject() {
                return new PoolableRectangle() {
                    @Override
                    public void reset() {
                        set(0, 0, 0, 0);
                    }
                };
            }
        };

        abstract class PoolableArray<T> extends Array<T> implements Pool.Poolable {}
        actorArrayPool = new Pool<Array<Actor>>() {
            @Override
            protected PoolableArray<Actor> newObject() {
                return new PoolableArray<Actor>() {
                    @Override
                    public void reset() {
                        clear();
                    }
                };
            }
        };

        abstract class PoolableColor extends Color implements Pool.Poolable {}
        colorPool = new Pool<Color>() {
            @Override
            protected Color newObject() {
                return new PoolableColor() {
                    @Override
                    public void reset() {
                        set(0, 0, 0, 1);
                    }
                };
            }
        };
        colorPool.fill(10);

        setScreen(new LoadingScreen(this));
    }

    @Override
    public void pause() {
        super.pause();
        nextScreen = getScreen();
    }

    @Override
    public void resume() {
        super.resume();
        setScreen(new LoadingScreen(this));
    }

    /**
     * Evento que un LoadingScreen lanza cuando ha cargado los recursos del juego.
     * @param loadingScreen La pantalla que ha lanzado el evento.
     */
    public void resourcesLoaded(LoadingScreen loadingScreen) {
        loadingScreen.dispose();

        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        } else {
            setScreen(new MainScreen(this));
        }
    }

    public void mapSelectionScreenChosen(MainScreen mainScreen) {
        mainScreen.dispose();

        LevelInfo level = null;
        for (LevelInfo currLevel : levelCatalog.levels()) {
            level = currLevel;
        }
        setScreen(new GameScreen(this, level));
    }

    /**
     * Evento que un GameScreen lanza cuando ha finalizado su actividad.
     * @param gameScreen La pantalla que ha lanzado el evento.
     * @param level Nivel en el que se ha jugado la partida.
     * @param paperCount Número de rollos de papel recolectados.
     * @param runningTime Tiempo de juego desde el final de la cuenta atrás hasta haber perdido.
     */
    public void gameOver(GameScreen gameScreen, LevelInfo level, int paperCount, float runningTime) {
        gameScreen.dispose();

        setScreen(new ResultsScreen(this, level, paperCount, runningTime));
    }

    /**
     * Evento que un ResultsScreen lanza cuando ha finalizado su actividad.
     * @param resultsScreen La pantalla que ha lanzado el evento.
     */
    public void resultsAccepted(ResultsScreen resultsScreen, boolean playAgain, LevelInfo sourceLevel) {
        resultsScreen.dispose();

        if (playAgain) {
            setScreen(new GameScreen(this, sourceLevel));
        } else {
            setScreen(new MainScreen(this));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) getScreen().dispose();
        assetManager.dispose();
        rectPool.clear();
        actorArrayPool.clear();
        colorPool.clear();
    }
}
