package com.example.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.testgame.screens.GameScreen;
import com.example.testgame.screens.LoadingScreen;

public class MyTestGame extends Game {
	// Radio ancho:alto de los viewports de las pantallas.
    public static final float V_RATIO = 5f/3f;
    // Tamaños de los viewports de las pantallas.
	public static final float V_WIDTH = 800;
	public static final float V_HEIGHT = V_WIDTH/V_RATIO;

	public SpriteBatch batch;
	public AssetManager assetManager;

	private LoadingScreen loadingScreen = null;
	private GameScreen gameScreen = null;

	@Override
	public void create() {
	    batch = new SpriteBatch();
		assetManager = new AssetManager();

		loadingScreen = new LoadingScreen(this);
	    setScreen(loadingScreen);
	}

	// Por lo de pronto solo utilizamos este método como transición de
	// la pantalla de carga a la del juego. Más adelante se ampliará
	// el sistema de carga de recursos para iniciar el proceso también
	// tras eventos como el pause/resume (que puede ocurrir al pulsar
	// el botón home y volver a entrar), pudiendo restaurar otras pantallas.
	public void resourcesLoaded() {
		gameScreen = new GameScreen(this);
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
