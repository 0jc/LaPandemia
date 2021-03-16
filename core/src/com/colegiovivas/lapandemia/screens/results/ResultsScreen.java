package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.*;

/**
 * Pantalla de los resultados de una partida. Muestra las estadísticas, informa al usuario
 * en caso de haber establecido un nuevo récord y, en tal caso, lo guarda junto con un nombre
 * elegido por él.
 */
public class ResultsScreen extends MultistateScreen<ResultsScreen.States> {
    public enum States {
        OPENING,
        TIME_SHOWN_MUSIC,
        TIME_INCREASING,
        TIME_STOPPED,
        SCORE_SHOWN,
        SCORE_INCREASING,
        SCORE_STOPPED,
        SHOWING_BUTTONS,
        CLOSING
    }

    /**
     * Vista de los resultados. Se establecen datos en ella y los representa
     * gráficamente, además de actuar como fuente de entrada de datos para
     * obtener el nick del jugador y el evento del click en el botón de continuar.
     */
    private final ResultsView resultsView;

    private boolean playAgain;

    public ResultsScreen(final LaPandemia main, final LevelInfo level, final AssetManager assetManager,
                         final int paperCount, final float runningTime)
    {
        this.resultsView = new ResultsView(assetManager, level);

        final Music backgroundMusic = assetManager.get("audio/results.wav");
        final Music highscoreMusic = assetManager.get("audio/claps.wav");
        final Music statShownMusic = assetManager.get("audio/stat-shown.wav");

        // Efecto sonoro que se reproduce cuando el valor de una estadística ha terminado de subir.
        final Music statReachedItsValue = assetManager.get("audio/stat-reached-its-value.wav");

        addState(States.OPENING, new StateAdapter() {
            private final Transition transition = new LeftOutTransition(0, 0.7f, 0);

            @Override
            public void enter() {
                backgroundMusic.setLooping(true);
                backgroundMusic.play();
            }

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                resultsView.getStage().draw();
                transition.draw();

                if (transition.isComplete()) {
                    setState(States.TIME_SHOWN_MUSIC);
                }
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        addState(States.TIME_SHOWN_MUSIC,
                new WaitMusicState<States>(statShownMusic, this, States.TIME_INCREASING)
        {
            @Override
            public void enter() {
                resultsView.setTimeVisible(true);
                super.enter();
            }

            @Override
            public void renderPlayingMusic(float delta) {
                resultsView.getStage().draw();
            }
        });

        addState(States.TIME_INCREASING, new StateAdapter() {
            private final CounterAnimator counterAnimator = new CounterAnimator(0.3f, 0 , runningTime);

            @Override
            public void render(float delta) {
                resultsView.getStage().draw();

                if (counterAnimator.isIncreasing()) {
                    resultsView.setTime(counterAnimator.update(delta));
                } else {
                    setState(States.TIME_STOPPED);
                }
            }
        });

        addState(States.TIME_STOPPED,
                new WaitMusicState<States>(statReachedItsValue, this, States.SCORE_SHOWN)
        {
            @Override
            public void renderPlayingMusic(float delta) {
                resultsView.getStage().draw();
            }
        });

        addState(States.SCORE_SHOWN,
                new WaitMusicState<States>(statShownMusic, this, States.SCORE_INCREASING)
        {
            @Override
            public void enter() {
                resultsView.setPaperCountVisible(true);
                super.enter();
            }

            @Override
            public void renderPlayingMusic(float delta) {
                resultsView.getStage().draw();
            }
        });

        addState(States.SCORE_INCREASING, new StateAdapter() {
            private final CounterAnimator counterAnimator = new CounterAnimator(0.3f, 0, paperCount);

            @Override
            public void render(float delta) {
                resultsView.getStage().draw();

                if (counterAnimator.isIncreasing()) {
                    int newValue = (int)counterAnimator.update(delta);
                    resultsView.setPaperCount(newValue);
                    if (newValue > level.getHighscore().getScore()) {
                        resultsView.setTitleColor(Color.GOLD);
                    }
                } else {
                    setState(States.SCORE_STOPPED);
                }
            }
        });

        addState(States.SCORE_STOPPED,
                new WaitMusicState<States>(statReachedItsValue, this, States.SHOWING_BUTTONS)
        {
            @Override
            public void renderPlayingMusic(float delta) {
                resultsView.getStage().draw();
            }
        });

        addState(States.SHOWING_BUTTONS, new StateAdapter() {
            @Override
            public void enter() {
                resultsView.setReturnButtonVisible(true);
                resultsView.setRetryButtonVisible(true);
                if (isNewHighscore(paperCount, level)) {
                    resultsView.setNicknameVisible(true);
                    highscoreMusic.setLooping(false);
                    highscoreMusic.play();
                }
            }

            @Override
            public void leave() {
                Gdx.input.setInputProcessor(new InputAdapter());
            }

            @Override
            public void show() {
                Gdx.input.setInputProcessor(resultsView.getStage());
            }

            @Override
            public void render(float delta) {
                resultsView.getStage().draw();
            }
        });

        addState(States.CLOSING, new StateAdapter() {
            private final Transition transition = new HCenterInTransition(0, 0.7f, 0.5f);

            @Override
            public void enter() {
                if (isNewHighscore(paperCount, level)) {
                    level.getHighscore().set(paperCount, resultsView.getNickname());
                }
            }

            @Override
            public void leave() {
                highscoreMusic.stop();
                backgroundMusic.stop();
            }

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                resultsView.getStage().draw();
                transition.draw();

                if (transition.isComplete()) {
                    main.resultsAccepted(ResultsScreen.this, playAgain, level);
                }
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        resultsView.addReturnListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playAgain = false;
                setState(States.CLOSING);
            }
        });

        resultsView.addRetryListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playAgain = true;
                setState(States.CLOSING);
            }
        });

        setState(States.OPENING);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        resultsView.getStage().getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        resultsView.getStage().dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    /**
     * @return True si y solo si la puntuación del usuario constituye un nuevo récord.
     */
    private static boolean isNewHighscore(int paperCount, LevelInfo level) {
        return level.getHighscore().getScore() < paperCount;
    }
}