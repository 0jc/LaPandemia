package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que, tras haber mostrado el mapa desde lejos, se hace zoom en
 * la dirección del personaje.
 */
public class ZoomInState extends MultistateScreen.StateAdapter {
    private final GameScreen gameScreen;
    private final OrthographicCamera camera;
    /**
     * Velocidad a la que se hace el zoom.
     */
    private final float speed;

    public ZoomInState(GameScreen gameScreen, float speed) {
        this.gameScreen = gameScreen;
        this.speed = speed;
        camera = (OrthographicCamera)gameScreen.getWorld().getStage().getCamera();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.draw();

        if (camera.zoom > 1) {
            camera.zoom = Math.max(camera.zoom - delta*speed, 1);
        } else if (!gameScreen.getIntroMusic().isPlaying()) {
            gameScreen.setState(GameScreen.STATE_WAIT_INTRO_MUSIC);
        }
    }
}
