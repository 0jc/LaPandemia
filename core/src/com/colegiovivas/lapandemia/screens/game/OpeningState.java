package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se reproduce la transición inicial y se empieza a
 * reproducir la música introductoria.
 */
public class OpeningState extends MultistateScreen.StateAdapter {
    private final GameScreen gameScreen;
    private final OrthographicCamera camera;

    public OpeningState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        camera = (OrthographicCamera)gameScreen.getWorld().getStage().getCamera();
    }

    @Override
    public void enter() {
        camera.zoom = gameScreen.getWorld().getMaxZoom();
        gameScreen.getOpeningTransition().start();
        gameScreen.getIntroMusic().setLooping(false);
        gameScreen.getIntroMusic().play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.getOpeningTransition().render(delta);

        gameScreen.draw();
        gameScreen.getOpeningTransition().draw();

        if (gameScreen.getOpeningTransition().isComplete()) {
            gameScreen.setState(GameScreen.STATE_ZOOM_IN);
        }
    }
}
