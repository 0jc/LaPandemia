package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.*;

/**
 * Pantalla de los resultados de una partida. Muestra las estadísticas, informa al usuario
 * en caso de haber establecido un nuevo récord y, en tal caso, lo guarda junto con un nombre
 * elegido por él.
 */
public class ResultsScreen extends MultistateScreen {
    static final int STATE_OPENING = 0;
    static final int STATE_TIME_VISIBLE = 1;
    static final int STATE_INCREASING_TIME = 2;
    static final int STATE_TIME_FINISHED_MUSIC = 3;
    static final int STATE_PAPER_COUNT_VISIBLE = 4;
    static final int STATE_INCREASING_PAPER_COUNT = 5;
    static final int STATE_PAPER_COUNT_FINISHED_MUSIC = 6;
    static final int STATE_SHOWING_BUTTONS = 7;
    static final int STATE_CLOSING = 8;

    private final LaPandemia main;

    /**
     * Nivel en el que transcurrió la partida.
     */
    private final LevelInfo level;

    /**
     * Número de rollos de papel recolectados.
     */
    private final int paperCount;

    /**
     * Duración de la partida en segundos.
     */
    private final float runningTime;

    /**
     * Vista de los resultados. Se establecen datos en ella y los representa
     * gráficamente, además de actuar como fuente de entrada de datos para
     * obtener el nick del jugador y el evento del click en el botón de continuar.
     */
    private final ResultsView resultsView;

    /**
     * Transición final.
     */
    private final Transition openingTransition;

    /**
     * Transición inicial.
     */
    private final Transition closingTransition;

    /**
     * Música de fondo que se reproduce en bucle.
     */
    private final Music backgroundMusic;

    /**
     * Efecto sonoro que se reproduce para dar la enhorabuena al usuario por establecer un
     * nuevo récord.
     */
    private final Music highscoreMusic;

    /**
     * Si al aceptar la pantalla se debe empezar una nueva partida en vez de volver al menú
     * principal.
     */
    private boolean playAgain;

    public ResultsScreen(LaPandemia main, LevelInfo level, int paperCount, float runningTime) {
        this.main = main;
        this.level = level;
        this.paperCount = paperCount;
        this.runningTime = runningTime;
        this.resultsView = new ResultsView(main, level);

        openingTransition = new LeftOutTransition(0, 0.7f, 0);
        closingTransition = new HCenterInTransition(0, 0.7f, 0.5f);

        // Efecto sonoro que se reproduce cuando el valor de una estadística ha terminado de subir.
        Music statReachedItsValue = main.assetManager.get("audio/stat-reached-its-value.wav");
        addState(STATE_OPENING, new OpeningState(this));
        addState(STATE_TIME_VISIBLE, new TimeVisibleState(main, this));
        addState(STATE_INCREASING_TIME, new IncreasingTimeState(this));
        addState(STATE_TIME_FINISHED_MUSIC, new WaitMusicState(statReachedItsValue,
                this, STATE_PAPER_COUNT_VISIBLE));
        addState(STATE_PAPER_COUNT_VISIBLE, new PaperCountVisibleState(main, this));
        addState(STATE_INCREASING_PAPER_COUNT, new IncreasingPaperCountState(this));
        addState(STATE_PAPER_COUNT_FINISHED_MUSIC, new WaitMusicState(statReachedItsValue,
                this, STATE_SHOWING_BUTTONS));
        addState(STATE_SHOWING_BUTTONS, new ShowingButtonsState(this));
        addState(STATE_CLOSING, new ClosingState(this));

        backgroundMusic = main.assetManager.get("audio/results.wav");
        highscoreMusic = main.assetManager.get("audio/claps.wav");

        setState(STATE_OPENING);
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

    /**
     * @return True si y solo si la puntuación del usuario constituye un nuevo récord.
     */
    public boolean isNewHighscore() {
        return level.getHighscore().getScore() < paperCount;
    }

    /**
     * @return El récord actual en el nivel jugado, sin tener en cuenta la marca establecida
     * en esta partida.
     */
    public int getHighscore() {
        return level.getHighscore().getScore();
    }

    /**
     * Guarda la marca actual como nuevo récord, utilizando el nombre introducido en el
     * formulario de la vista.
     */
    public void saveScore() {
        level.getHighscore().set(paperCount, resultsView.getNickname());
    }

    public Transition getOpeningTransition() {
        return openingTransition;
    }

    public Transition getClosingTransition() {
        return closingTransition;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public Music getHighscoreMusic() {
        return highscoreMusic;
    }

    public float getRunningTime() {
        return runningTime;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPlayAgain(boolean playAgain) {
        this.playAgain = playAgain;
    }

    /**
     * Devuelve el control a la clase principal.
     */
    public void finish() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.resultsAccepted(this, playAgain, level);
    }
}
