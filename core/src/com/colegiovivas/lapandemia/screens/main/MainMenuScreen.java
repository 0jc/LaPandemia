package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.Gdx;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

public class MainMenuScreen extends MultistateScreen {
    static final int STAGE_OPENING = 1;
    static final int STAGE_IDLE = 2;
    static final int STAGE_CLOSING = 3;

    private final LaPandemia main;
    private final MainMenuView mainMenuView;
    private final Transition openingTransition;

    public MainMenuScreen(LaPandemia main) {
        this.main = main;

        openingTransition = new HCenterOutTransition(0, 0.7f, 0);

        mainMenuView = new MainMenuView(main);

        addState(STAGE_OPENING, new OpeningState(this));
        addState(STAGE_IDLE, new IdleState(main, this));

        mainMenuView.setPlayListener(new MainMenuView.PlayListener() {
            @Override
            public void playClicked() {
                MainMenuScreen.this.main.mapSelectionScreenChosen(MainMenuScreen.this);
            }
        });

        setState(STAGE_OPENING);
    }

    @Override
    public void resize(int width, int height) {
        openingTransition.getViewport().update(width, height);
        mainMenuView.getStage().getViewport().update(width, height);
        super.resize(width, height);
    }

    public void draw() {
        mainMenuView.getStage().draw();
        openingTransition.draw();
    }

    public MainMenuView getMainMenuView() {
        return mainMenuView;
    }

    public Transition getOpeningTransition() {
        return openingTransition;
    }

    @Override
    public void dispose() {
        mainMenuView.dispose();
    }
}
