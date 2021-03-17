package com.colegiovivas.lapandemia.screens.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.TopInTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;

/**
 * Pantalla en la que se muestran los datos del récord en un nivel y que incluye
 * un botón para empezar una partida en él.
 */
public class PreviewScreen extends MultistateScreen<PreviewScreen.States> {
    /**
     * Estados en los que se puede encontrar la pantalla.
     */
    public enum States { IDLE, LEVEL_CHOSEN, CLOSING }

    /**
     * Vista de la pantalla.
     */
    private final PreviewView previewView;

    /**
     * Inicializa la pantalla.
     * @param main Clase principal a la que se notifica para volver atrás o empezar una partida.
     * @param levelInfo Nivel cuyos datos se muestran en la pantalla y en el que se podrá jugar.
     * @param assetManager Gestor de recursos de la aplicación, necesario para renderizar la pantalla.
     */
    public PreviewScreen(final LaPandemia main, final LevelInfo levelInfo, AssetManager assetManager) {
        previewView = new PreviewView(assetManager, levelInfo);

        final Music backgroundMusic = assetManager.get("audio/menu-misc.mp3");
        final Music levelChosenMusic = assetManager.get("audio/level-chosen.wav");

        final InputAdapter noInput = new InputAdapter();

        final InputMultiplexer inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(previewView.getStage());
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    main.navigatedBackToMapSelection(PreviewScreen.this);
                    return true;
                }

                return false;
            }
        });

        addState(States.IDLE, new StateAdapter() {
            @Override
            public void show() {
                Gdx.input.setCatchKey(Input.Keys.BACK, true);
                Gdx.input.setInputProcessor(inputProcessor);
            }

            @Override
            public void leave() {
                Gdx.input.setCatchKey(Input.Keys.BACK, false);
                Gdx.input.setInputProcessor(noInput);
            }

            @Override
            public void render(float delta) {
                previewView.getStage().draw();
            }
        });

        addState(States.LEVEL_CHOSEN, new StateAdapter() {
            @Override
            public void enter() {
                backgroundMusic.stop();
                levelChosenMusic.play();
            }

            @Override
            public void render(float delta) {
                previewView.getStage().draw();

                if (!levelChosenMusic.isPlaying()) {
                    setState(States.CLOSING);
                }
            }
        });

        addState(States.CLOSING, new StateAdapter() {
            private final Transition transition = new TopInTransition(0.4f, 1f, 0.2f);

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                previewView.getStage().draw();
                transition.draw();

                if (transition.isComplete()) {
                    main.mapChosen(PreviewScreen.this, levelInfo);
                }
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        previewView.addStartListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setState(States.LEVEL_CHOSEN);
            }
        });

        setState(States.IDLE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        previewView.getStage().getViewport().update(width, height);

        super.resize(width, height);
    }

    @Override
    public void dispose() {
        previewView.dispose();

        super.dispose();
    }
}
