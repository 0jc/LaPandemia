package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.audio.Music;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

/**
 * Pantalla principal del juego.
 */
public class MainMenuScreen extends MultistateScreen {
    static final int STATE_OPENING = 1;
    static final int STATE_IDLE = 2;

    private final LaPandemia main;
    private final MainMenuView mainMenuView;

    /**
     * Música de fondo. Se empieza a reproducir en esta pantalla pero son otros submenús los
     * que deciden cuándo pararla.
     */
    private final Music backgroundMusic;

    /**
     * True si se debe reproducir la transición inicial, openingTransition, o false en caso contrario.
     * Se utiliza para poder transicionar de forma intuitiva desde otras pantallas con transición como
     * la de los resultados de una partida, pero no por ejemplo al volver del menú de selección de
     * mapas.
     */
    private final boolean playOpeningTransition;

    /**
     * Transición que se realiza para introducir la pantalla en caso de que playOpeningTransition
     * sea verdadero.
     */
    private final Transition openingTransition;

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
        addState(STATE_IDLE, new IdleState(this));

        mainMenuView.setPlayListener(new MainMenuView.PlayListener() {
            @Override
            public void playClicked() {
                MainMenuScreen.this.main.mapSelectionScreenChosen(MainMenuScreen.this);
            }
        });

        mainMenuView.setCreditsListener(new MainMenuView.CreditsListener() {
            @Override
            public void creditsClicked() {
                MainMenuScreen.this.main.creditsScreenChosen(MainMenuScreen.this);
            }
        });

        mainMenuView.setSettingsListener(new MainMenuView.SettingsListener() {
            @Override
            public void settingsClicked() {
                MainMenuScreen.this.main.settingsScreenChosen(MainMenuScreen.this);
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
