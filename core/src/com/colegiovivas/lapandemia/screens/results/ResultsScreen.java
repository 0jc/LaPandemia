package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.StagedScreen;
import com.colegiovivas.lapandemia.screens.transitions.LeftInTransition;
import com.colegiovivas.lapandemia.screens.transitions.LeftOutTransition;

public class ResultsScreen extends StagedScreen {
    static final int STAGE_OPENING = 0;
    static final int STAGE_TIME_VISIBLE = 1;
    static final int STAGE_TIME_PRE_WAIT = 2;
    static final int STAGE_INCREASING_TIME = 3;
    static final int STAGE_TIME_POST_WAIT = 4;
    static final int STAGE_PAPER_COUNT_VISIBLE = 5;
    static final int STAGE_PAPER_COUNT_PRE_WAIT = 6;
    static final int STAGE_INCREASING_PAPER_COUNT = 7;
    static final int STAGE_PAPER_COUNT_POST_WAIT = 8;
    static final int STAGE_SHOWING_CONTINUE_BUTTON = 9;
    static final int STAGE_CLOSING = 10;

    private final LaPandemia main;
    private final LevelInfo level;
    private final int paperCount;
    private final float runningTime;
    private final ResultsView resultsView;
    private final LeftOutTransition openingTransition;
    private final LeftInTransition closingTransition;
    private final Music backgroundMusic;

    public ResultsScreen(LaPandemia main, LevelInfo level, int paperCount, float runningTime) {
        this.main = main;
        this.level = level;
        this.paperCount = paperCount;
        this.runningTime = runningTime;
        this.resultsView = new ResultsView(main, level);

        openingTransition = new LeftOutTransition(900);
        closingTransition = new LeftInTransition(900);

        addGameStage(STAGE_OPENING, new OpeningGameStage(this));
        addGameStage(STAGE_TIME_VISIBLE, new TimeVisibleGameStage(this, true));
        addGameStage(STAGE_TIME_PRE_WAIT, new WaitGameStage(this, 0.5f, STAGE_INCREASING_TIME));
        addGameStage(STAGE_INCREASING_TIME, new IncreasingTimeGameStage(this));
        addGameStage(STAGE_TIME_POST_WAIT, new WaitGameStage(this, 0.5f, STAGE_PAPER_COUNT_VISIBLE));
        addGameStage(STAGE_PAPER_COUNT_VISIBLE, new PaperCountVisibleGameStage(this, true));
        addGameStage(STAGE_PAPER_COUNT_PRE_WAIT, new WaitGameStage(this, 0.5f, STAGE_INCREASING_PAPER_COUNT));
        addGameStage(STAGE_INCREASING_PAPER_COUNT, new IncreasingPaperCountGameStage(this));
        addGameStage(STAGE_PAPER_COUNT_POST_WAIT, new WaitGameStage(this, 0.5f, STAGE_SHOWING_CONTINUE_BUTTON));
        addGameStage(STAGE_SHOWING_CONTINUE_BUTTON, new ShowingContinueButtonGameStage(this));
        addGameStage(STAGE_CLOSING, new ClosingGameStage(this));

        backgroundMusic = main.assetManager.get("audio/results.wav");

        setGameStage(STAGE_OPENING);
    }

    public void draw() {
        resultsView.getStage().draw();
    }

    public ResultsView getResultsView() {
        return resultsView;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        resultsView.getStage().getViewport().update(width, height);
        openingTransition.getViewport().update(width, height);
        closingTransition.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        resultsView.getStage().dispose();
        openingTransition.dispose();
        closingTransition.dispose();
    }

    public LeftOutTransition getOpeningTransition() {
        return openingTransition;
    }

    public LeftInTransition getClosingTransition() {
        return closingTransition;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public float getRunningTime() {
        return runningTime;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void finish() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundMusic.stop();
        main.resultsAccepted(this);
    }
}
