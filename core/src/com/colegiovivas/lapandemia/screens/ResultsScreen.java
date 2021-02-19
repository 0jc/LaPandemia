package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;

public class ResultsScreen extends StagedScreen {
    private static final int TITLE_H = 75;

    private final LaPandemia parent;
    private final int levelId;
    private final int paperCount;
    private final float runningTime;
    private final ResultsSubscreen resultsSubscreen;
    private final RectanglesTransition startTransition;
    private final RectanglesTransition endTransition;

    public ResultsScreen(LaPandemia parent, int levelId, int paperCount, float runningTime) {
        super();

        this.parent = parent;
        this.levelId = levelId;
        this.paperCount = paperCount;
        this.runningTime = runningTime;

        resultsSubscreen = new ResultsSubscreen(parent, levelId);
        resultsSubscreen.setScreenBounds(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addSubscreen(resultsSubscreen);

        startTransition = new RectanglesTransition(240, RectanglesTransition.Dir.OUT, true, 300);
        startTransition.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addSubscreen(startTransition);

        endTransition = new RectanglesTransition(400, RectanglesTransition.Dir.IN, false, 600);
        endTransition.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addSubscreen(endTransition);

        setGameStage(new IntroductionGameStage());
    }

    private class IntroductionGameStage extends GameStage {
        @Override
        void render(float delta) {
            Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (!startTransition.isPlaying()) {
                startTransition.start();
            } else {
                startTransition.act(delta);
            }

            resultsSubscreen.draw(delta);
            startTransition.draw(delta);

            if (!startTransition.isPlaying()) {
                setGameStage(new ShowStats());
            }
        }
    }

    private class ShowStats extends GameStage {
        private CounterAnimator timeCounter;
        private CounterAnimator paperCounter;
        private float waitedTime;

        public ShowStats() {
            timeCounter = new CounterAnimator(0.3f, 0, runningTime);
            paperCounter = new CounterAnimator(0.3f, 0, paperCount);
            waitedTime = 0;
        }

        @Override
        void show() {
            Gdx.input.setInputProcessor(resultsSubscreen.getInputProcessor());
        }

        @Override
        void render(float delta) {
            Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (!resultsSubscreen.showingTime()) {
                resultsSubscreen.setTime(timeCounter.getStartValue());
                resultsSubscreen.showTime();
            } else if (timeCounter.isIncreasing()) {
                resultsSubscreen.setTime(timeCounter.update(delta));
            } else if (!resultsSubscreen.showingPaperCount()) {
                resultsSubscreen.setPaperCount((int)paperCounter.getStartValue());
                resultsSubscreen.showPaperCount();
            } else if (paperCounter.isIncreasing()) {
                resultsSubscreen.setPaperCount((int)paperCounter.update(delta));
            } else if (waitedTime < 0.5f) {
                waitedTime += delta;
            } else if (!resultsSubscreen.showingContinueButton()) {
                resultsSubscreen.showContinueButton();
            }

            resultsSubscreen.draw(delta);

            if (resultsSubscreen.isAccepted()) {
                setGameStage(new LeavingGameStage());
            }
        }
    }

    private class LeavingGameStage extends GameStage {
        @Override
        void render(float delta) {
            Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (!endTransition.isPlaying()) {
                endTransition.start();
            } else {
                endTransition.act(delta);
            }

            resultsSubscreen.draw(delta);
            endTransition.draw(delta);

            if (!endTransition.isPlaying()) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                parent.resultsAccepted(ResultsScreen.this);
            }
        }
    }


    private class CounterAnimator {
        private final float totalTime;
        private final float startValue;
        private final float endValue;
        private float currentTime;

        public CounterAnimator(float totalTime, float startValue, float endValue) {
            this.totalTime = totalTime;
            this.startValue = startValue;
            this.endValue = endValue;
            currentTime = 0;
        }

        public float update(float delta) {
            currentTime = Math.min(currentTime + delta, totalTime);
            return startValue + (endValue - startValue)*(currentTime/totalTime);
        }

        public boolean isIncreasing() {
            return currentTime < totalTime;
        }

        public float getStartValue() {
            return startValue;
        }
    }
}
