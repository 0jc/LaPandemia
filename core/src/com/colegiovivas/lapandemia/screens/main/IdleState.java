package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;

public class IdleState extends MultistateScreen.StateAdapter {
    private final MainMenuScreen mainMenuScreen;
    private final InputProcessor noInput;

    public IdleState(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
        noInput = new InputAdapter();
    }

    @Override
    public void leave() {
        Gdx.input.setInputProcessor(noInput);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuScreen.getMainMenuView().getStage());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainMenuScreen.draw();
    }
}
