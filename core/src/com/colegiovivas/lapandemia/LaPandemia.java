package com.colegiovivas.lapandemia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    public SpriteBatch batch;
    public AssetManager assetManager;
    public Pool<Rectangle> rectPool;
    public Pool<Array<Actor>> actorArrayPool;

    private Screen nextScreen = null;

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

    public void resourcesLoaded(LoadingScreen loadingScreen) {
        loadingScreen.dispose();

        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        } else {
            setScreen(new GameScreen(this, getDemoLevel()));
        }
    }

    public void gameOver(GameScreen gameScreen, int paperCount, float runningTime) {
        Gdx.app.log("LaPandemia", "Game results: paperCount=" + paperCount + ", runningTime=" + runningTime);
        gameScreen.dispose();
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) getScreen().dispose();
        batch.dispose();
        assetManager.dispose();
        rectPool.clear();
        actorArrayPool.clear();
    }

    private Level getDemoLevel() {
        Level level = new Level();
        level.width = 32*47;
        level.height = 32*39;
        level.startX = 106;
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

        level.fans.add(new Fan(32 + 10, 32 + 10));
        level.fans.add(new Fan(32*44 - 10, 32 + 10));
        level.fans.add(new Fan(32*(1 + 6 + 1) + 10, 32*(1 + 6 + 1) + 10));
        level.fans.add(new Fan(32*(1 + 6 + 15 + 3 + 12) - 10, 32*(1 + 6 + 1) + 10));
        level.fans.add(new Fan(32 + 10, 32*36 - 10));
        level.fans.add(new Fan(32*44 - 10, 32*36 - 10));
        level.fans.add(new Fan(32*(1 + 6 + 15 + 2) + 10, 32*(1 + 6 + 1 + 21) - 10));
        level.fans.add(new Fan(32*(1 + 6 + 14) - 10, 32*(1 + 6 + 1 + 21) - 10));

        level.walls.add(new Wall(0, 0, level.width, 32));
        level.walls.add(new Wall(0, 32, 32, level.height - 32));
        level.walls.add(new Wall(32, level.height - 32, level.width - 32, 32));
        level.walls.add(new Wall(level.width - 32, 32, 32, level.height - 64));

        return level;
    }
}
