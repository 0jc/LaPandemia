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
import com.colegiovivas.lapandemia.screens.results.ResultsScreen;
import com.colegiovivas.lapandemia.screens.game.GameScreen;
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
            //setScreen(new ResultsScreen(this, 12345, 1234234234, 23908f));
            setScreen(new GameScreen(this, 1, Gdx.files.internal("levels/test.json")));
        }
    }

    public void gameOver(GameScreen gameScreen, int levelId, int paperCount, float runningTime) {
        gameScreen.dispose();

        setScreen(new ResultsScreen(this, levelId, paperCount, runningTime));
    }

    public void resultsAccepted(ResultsScreen resultsScreen) {
        resultsScreen.dispose();

        setScreen(null);
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
}
