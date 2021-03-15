package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.TopInTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

/**
 * Pantalla que muestra los datos del récord en un nivel e incluye un botón para
 * empezar una partida en él.
 */
public class PreviewScreen extends MultistateScreen {
    static final int STATE_IDLE = 0;
    static final int STATE_LEVEL_CHOSEN_MUSIC = 1;
    static final int STATE_CLOSING = 2;

    private final LaPandemia main;
    private final LevelInfo levelInfo;
    private final PreviewView previewView;
    private final Transition closingTransition;
    private final Music backgroundMusic;
    private final Music levelChosenMusic;

    public PreviewScreen(LaPandemia main, LevelInfo levelInfo) {
        this.main = main;
        this.levelInfo = levelInfo;

        closingTransition = new TopInTransition(0.4f, 1f, 0.2f);
        backgroundMusic = main.getAssetManager().get("audio/menu-misc.mp3");
        levelChosenMusic = main.getAssetManager().get("audio/level-chosen.wav");

        previewView = new PreviewView(main, this, levelInfo);

        previewView.setPlayListener(new PreviewView.PlayListener() {
            @Override
            public void playClicked() {
                setState(STATE_LEVEL_CHOSEN_MUSIC);
            }
        });

        addState(STATE_IDLE, new IdleState(main, this));
        addState(STATE_LEVEL_CHOSEN_MUSIC, new LevelChosenMusicState(main, this));
        addState(STATE_CLOSING, new ClosingState(main, this));

        setState(STATE_IDLE);
    }

    /**
     * @return La música que se está reproduciendo de fondo (lanzada desde el menú principal).
     */
    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * @return La música que se reproduce al elegir jugar en el nivel.
     */
    public Music getLevelChosenMusic() {
        return levelChosenMusic;
    }

    /**
     * @return La transición que se reproduce al elegir empezar la partida.
     */
    public Transition getClosingTransition() {
        return closingTransition;
    }

    /**
     * @return La vista de la pantalla.
     */
    public PreviewView getPreviewView() {
        return previewView;
    }

    @Override
    public void resize(int width, int height) {
        closingTransition.getViewport().update(width, height);
        previewView.getStage().getViewport().update(width, height);
        super.resize(width, height);
    }

    /**
     * Dibuja el estado actual de la pantalla.
     */
    public void draw() {
        previewView.getStage().draw();
        closingTransition.draw();
    }

    @Override
    public void dispose() {
        previewView.dispose();
        closingTransition.dispose();
    }

    /**
     * Transfiere el control de vuelta a la clase principal para volver al menú de
     * selección de mapa.
     */
    public void back() {
        main.navigatedBackToMapSelection(this);
    }

    /**
     * Transfiere el control de vuelta a la clase principal para volver al menú de
     * selección de mapa.
     */
    public void play() {
        main.mapChosen(this, levelInfo);
    }
}
