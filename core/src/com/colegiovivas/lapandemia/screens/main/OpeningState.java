package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

/**
 * Estado en el que se encuentra la pantalla durante la transición inicial.
 * En caso de que se haya decidido desde MainMenuScreen que no se realizará
 * esta transición, se salta directamente al siguiente estado.
 */
public class OpeningState extends MultistateScreen.StateAdapter {
    private final MainMenuScreen mainMenuScreen;

    public OpeningState(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void enter() {
        if (mainMenuScreen.getPlayOpeningTransition()) {
            mainMenuScreen.getOpeningTransition().start();
        }
    }

    @Override
    public void show() {
        mainMenuScreen.getBackgroundMusic().play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainMenuScreen.getOpeningTransition().render(delta);

        mainMenuScreen.draw();

        if (!mainMenuScreen.getPlayOpeningTransition() || mainMenuScreen.getOpeningTransition().isComplete()) {
            mainMenuScreen.setState(MainMenuScreen.STATE_IDLE);
        }
    }
}
