package com.colegiovivas.lapandemia.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

/**
 * Pantalla principal del juego que se muestra, entre otras situaciones, al iniciar la aplicación.
 */
public class MainMenuScreen extends MultistateScreen<MainMenuScreen.States> {
    /**
     * Estados en los que se puede encontrar la pantalla.
     */
    public enum States { OPENING, IDLE }

    /**
     * Vista de la pantalla.
     */
    private final MainMenuView mainMenuView;

    /**
     * Inicializa la pantalla.
     * @param main Clase principal a la que se notifica del submenú elegido.
     * @param assetManager Gestor de recursos de la aplicación, necesario para renderizar la pantalla.
     */
    public MainMenuScreen(LaPandemia main, AssetManager assetManager) {
        this(main, assetManager, false);
    }

    /**
     * Inicializa la pantalla.
     * @param main Clase principal a la que se notifica del submenú elegido.
     * @param assetManager Gestor de recursos de la aplicación, necesario para renderizar la pantalla.
     * @param playTransition True si y solo si se debe realizar la transición inicial. Esto es deseable
     *                       cuando la pantalla previa también había realizado una transición, para no
     *                       hacer la presentación demasiado brusca.
     */
    public MainMenuScreen(final LaPandemia main, AssetManager assetManager, final boolean playTransition) {
        mainMenuView = new MainMenuView(assetManager);

        final InputProcessor noInput = new InputAdapter();
        final Music backgroundMusic = assetManager.get("audio/menu-misc.mp3");
        backgroundMusic.setLooping(true);

        addState(States.OPENING, new StateAdapter() {
            private final Transition transition = new HCenterOutTransition(0, 0.7f, 0);

            @Override
            public void show() {
                backgroundMusic.play();
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                mainMenuView.getStage().draw();
                transition.draw();

                if (transition.isComplete()) {
                    setState(States.IDLE);
                }
            }

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        addState(States.IDLE, new StateAdapter() {
            @Override
            public void show() {
                backgroundMusic.play();
                Gdx.input.setInputProcessor(mainMenuView.getStage());
            }

            @Override
            public void leave() {
                Gdx.input.setInputProcessor(noInput);
            }

            @Override
            public void render(float delta) {
                mainMenuView.getStage().draw();
            }
        });

        mainMenuView.addPlayListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                main.mapSelectionScreenChosen(MainMenuScreen.this);
            }
        });

        mainMenuView.addSettingsListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                main.settingsScreenChosen(MainMenuScreen.this);
            }
        });

        mainMenuView.addCreditsListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                main.creditsScreenChosen(MainMenuScreen.this);
            }
        });

        setState(playTransition ? States.OPENING : States.IDLE);
    }

    @Override
    public void resize(int width, int height) {
        mainMenuView.getStage().getViewport().update(width, height);

        super.resize(width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    @Override
    public void dispose() {
        mainMenuView.dispose();

        super.dispose();
    }
}
