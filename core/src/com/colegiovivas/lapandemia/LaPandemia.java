package com.colegiovivas.lapandemia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.colegiovivas.lapandemia.levels.Fan;
import com.colegiovivas.lapandemia.levels.Level;
import com.colegiovivas.lapandemia.levels.Wall;
import com.colegiovivas.lapandemia.screens.GameScreen;
import com.colegiovivas.lapandemia.screens.LoadingScreen;

public class LaPandemia extends Game {
    // Radio ancho:alto de los viewports de las pantallas.
    public static final float V_RATIO = 5f / 3f;
    // Tamaños de los viewports de las pantallas.
    public static final float V_WIDTH = 800;
    public static final float V_HEIGHT = V_WIDTH / V_RATIO;

    public SpriteBatch batch;
    public AssetManager assetManager;
    public Pool<Rectangle> rectPool;
    public Pool<Array<Actor>> actorArrayPool;

    private LoadingScreen loadingScreen = null;
    private GameScreen gameScreen = null;

    @Override
    public void create() {
        Gdx.app.log("LaPandemia", "create()");
        batch = new SpriteBatch();
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

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    // Por lo de pronto solo utilizamos este método como transición de
    // la pantalla de carga a la del juego. Más adelante se ampliará
    // el sistema de carga de recursos para iniciar el proceso también
    // tras eventos como el pause/resume (que puede ocurrir al pulsar
    // el botón home y volver a entrar), pudiendo restaurar otras pantallas.
    public void resourcesLoaded() {
        gameScreen = new GameScreen(this, getDemoLevel());
        setScreen(gameScreen);

        loadingScreen.dispose();
        loadingScreen = null;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (loadingScreen != null) loadingScreen.dispose();
        if (gameScreen != null) gameScreen.dispose();
        batch.dispose();
        assetManager.dispose();
        rectPool.clear();
        actorArrayPool.clear();
    }

    private Level getDemoLevel() {
        Level level = new Level();
        level.width = 32*47;
        level.height = 32*39;
        level.startX = 32;
        level.startY = 32;
        level.startXDir = 1;
        level.startYDir = 0;

        level.walls.add(new Wall(7, 7, 15, 1, 32));
        level.walls.add(new Wall(25, 7, 15, 1, 32));
        level.walls.add(new Wall(7, 8, 1, 15, 32));
        level.walls.add(new Wall(39, 8, 1, 15, 32));
        level.walls.add(new Wall(8, 22, 11, 1, 32));
        level.walls.add(new Wall(28, 22, 11, 1, 32));
        level.walls.add(new Wall(23, 19, 1, 12, 32));
        level.walls.add(new Wall(7, 31, 33, 1, 32));

        level.fans.add(new Fan(32*44 - 10, 32 + 10));
        level.fans.add(new Fan(32*(1 + 6 + 15 + 3 + 12) - 10, 32*(1 + 6 + 1) + 10));
        level.fans.add(new Fan(32 + 10, 32*36 - 10));
        level.fans.add(new Fan(32*(1 + 6 + 15 + 2) + 10, 32*(1 + 6 + 1 + 21) - 10));

        level.walls.add(new Wall(0, 0, level.width, 32));
        level.walls.add(new Wall(0, 32, 32, level.height - 32));
        level.walls.add(new Wall(32, level.height - 32, level.width - 32, 32));
        level.walls.add(new Wall(level.width - 32, 32, 32, level.height - 64));

        return level;
    }
}
