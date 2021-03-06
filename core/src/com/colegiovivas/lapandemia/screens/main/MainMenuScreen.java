package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

public class MainMenuScreen extends MultistateScreen {
    static final int STATE_OPENING = 1;
    static final int STATE_IDLE = 2;

    private final LaPandemia main;
    private final MainMenuView mainMenuView;
    private final Transition openingTransition;
    private final Music backgroundMusic;
    private final boolean playOpeningTransition;

    public MainMenuScreen(LaPandemia main) {
        this(main, false);
    }

    public MainMenuScreen(LaPandemia main, boolean playOpeningTransition) {
        this.main = main;
        this.playOpeningTransition = playOpeningTransition;

        openingTransition = new HCenterOutTransition(0, 0.7f, 0);

        mainMenuView = new MainMenuView(main);

        backgroundMusic = main.assetManager.get("audio/menu-misc.mp3");
        backgroundMusic.setLooping(true);

        addState(STATE_OPENING, new OpeningState(this));
        addState(STATE_IDLE, new IdleState(main, this));

        mainMenuView.setPlayListener(new MainMenuView.PlayListener() {
            @Override
            public void playClicked() {
                MainMenuScreen.this.main.mapSelectionScreenChosen(MainMenuScreen.this);
            }
        });

        setState(STATE_OPENING);
    }

    public boolean getPlayOpeningTransition() {
        return playOpeningTransition;
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

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    @Override
    public void dispose() {
        mainMenuView.dispose();
        openingTransition.dispose();
    }
}
