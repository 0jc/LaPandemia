package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.TopInTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

public class PreviewScreen extends MultistateScreen {
    static final int STATE_IDLE = 0;
    static final int STATE_CLOSING = 1;

    private final LaPandemia main;
    private final LevelInfo levelInfo;
    private final PreviewView previewView;
    private final Transition closingTransition;
    private final Music backgroundMusic;

    public PreviewScreen(LaPandemia main, LevelInfo levelInfo) {
        this.main = main;
        this.levelInfo = levelInfo;

        closingTransition = new TopInTransition(0, 1f, 0.2f);
        backgroundMusic = main.assetManager.get("audio/menu-misc.mp3");

        previewView = new PreviewView(main, this, levelInfo);

        previewView.setPlayListener(new PreviewView.PlayListener() {
            @Override
            public void playClicked() {
                setState(STATE_CLOSING);
            }
        });

        addState(STATE_IDLE, new IdleState(main, this));
        addState(STATE_CLOSING, new ClosingState(main, this));

        setState(STATE_IDLE);
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public Transition getClosingTransition() {
        return closingTransition;
    }

    public PreviewView getPreviewView() {
        return previewView;
    }

    @Override
    public void resize(int width, int height) {
        closingTransition.getViewport().update(width, height);
        previewView.getStage().getViewport().update(width, height);
        super.resize(width, height);
    }

    public void draw() {
        previewView.getStage().draw();
        closingTransition.draw();
    }

    @Override
    public void dispose() {
        previewView.dispose();
        closingTransition.dispose();
    }

    public void back() {
        main.navigatedBackToMapSelection(this);
    }

    public void play() {
        main.mapChosen(this, levelInfo);
    }
}
