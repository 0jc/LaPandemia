package com.colegiovivas.lapandemia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.colegiovivas.lapandemia.pooling.ArrayPool;
import com.colegiovivas.lapandemia.pooling.CollisionInfoPool;
import com.colegiovivas.lapandemia.pooling.RectanglePool;
import com.colegiovivas.lapandemia.level.Level;
import com.colegiovivas.lapandemia.level.Fan;
import com.colegiovivas.lapandemia.level.Wall;
import com.colegiovivas.lapandemia.pooling.VirusPool;
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
    public RectanglePool rectPool;
    public ArrayPool<Actor> actorArrayPool;
    public CollisionInfoPool collisionInfoPool;
    public VirusPool virusPool;

    private LoadingScreen loadingScreen = null;
    private GameScreen gameScreen = null;

    @Override
    public void create() {
        Gdx.app.log("LaPandemia", "create()");
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        rectPool = new RectanglePool();
        actorArrayPool = new ArrayPool<>();
        collisionInfoPool = new CollisionInfoPool(this);
        virusPool = new VirusPool(this);

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    // Por lo de pronto solo utilizamos este método como transición de
    // la pantalla de carga a la del juego. Más adelante se ampliará
    // el sistema de carga de recursos para iniciar el proceso también
    // tras eventos como el pause/resume (que puede ocurrir al pulsar
    // el botón home y volver a entrar), pudiendo restaurar otras pantallas.
    public void resourcesLoaded() {
        Level testLevel = new Level();
        testLevel.width = 1600 + 2 * 32;
        testLevel.height = 960 + 2 * 32;
        testLevel.startX = 800;
        testLevel.startY = 480;
        testLevel.startXDir = 0;
        testLevel.startYDir = 1;
        testLevel.maxVirusCount = 300;
        testLevel.walls.add(new Wall(600, 320, 32 * 10, 32 * 5));

        testLevel.walls.add(new Wall(0, 0, testLevel.width, 32));
        testLevel.walls.add(new Wall(0, 32, 32, testLevel.height - 32));
        testLevel.walls.add(new Wall(32, testLevel.height - 32, testLevel.width - 32, 32));
        testLevel.walls.add(new Wall(testLevel.width - 32, 32, 32, testLevel.height - 64));

        testLevel.fans.add(new Fan(728, 640));

        gameScreen = new GameScreen(this, testLevel);
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
    }
}
