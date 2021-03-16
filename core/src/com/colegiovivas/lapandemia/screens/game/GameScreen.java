package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.gestures.MovePlayerGestureListener;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import com.colegiovivas.lapandemia.hardware.HardwareWrapper;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.Transition;
import com.colegiovivas.lapandemia.screens.transitions.VCenterInTransition;

/**
 * Pantalla de juego. Se muestra en ella un mapa cargado junto a una pequeña
 * interfaz en el borde superior con estadísticas de la partida y otra información
 * relevante para el usuario.
 */
public class GameScreen extends MultistateScreen<GameScreen.States> {
    public enum States {
        OPENING,
        ZOOMING_IN,
        WAITING_INTRO_MUSIC,
        PAUSING_BEFORE_COUNTDOWN,
        COUNTDOWN,
        PLAYING,
        PAUSED,
        GAME_OVER_MUSIC,
        CLOSING
    }

    /**
     * Altura de la banda informativa superior.
     */
    private static final int STATS_H = 75;

    /**
     * Valor del giroscopio sobre el eje Y a partir del que se considera que el
     * usuario está realizando el gesto de pausa o reanudación de la partida.
     * Los valores válidos son este y aquellos que sean inferiores.
     */
    static final float Y_GYROSCOPE_PAUSE_TRESHOLD = -6;

    private Music currentGameMusic;

    private final World world;
    private final GameStats stats;
    private final Countdown countdown;

    public GameScreen(final LaPandemia main, final LevelInfo level, final HardwareWrapper hardwareWrapper,
                      final AssetManager assetManager)
    {
        world = new World(assetManager, hardwareWrapper, level);
        stats = new GameStats(assetManager);
        countdown = new Countdown(assetManager);
        final Music introMusic = assetManager.get("audio/game-opening.wav");
        final Music gameOverMusic = assetManager.get("audio/game-over.wav");
        final Music mapMusic = assetManager.get("audio/map.wav");
        final Music invincibleMusic = assetManager.get("audio/ticking.wav");
        final Sound pauseSound = assetManager.get("audio/pause.wav");
        final OrthographicCamera camera = (OrthographicCamera)world.getStage().getCamera();

        final InputProcessor noInput = new InputAdapter();
        final InputProcessor pausedInputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    setState(States.PLAYING);
                    return true;
                }

                return false;
            }
        };
        final InputMultiplexer playingInputProcessor = new InputMultiplexer();
        // ZoomGestureListener debe ir antes de MovePlayerGestureListener, ya que el primero
        // decide quién de los dos debe procesar los gestos tap.
        playingInputProcessor.addProcessor(new GestureDetector(new ZoomGestureListener(world)));
        playingInputProcessor.addProcessor(new GestureDetector(new MovePlayerGestureListener(world.getPlayerActor())));
        playingInputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    setState(States.PAUSED);
                    return true;
                }

                return false;
            }
        });

        currentGameMusic = mapMusic;

        world.getPlayerActor().setPowerupListener(new PlayerActor.PowerupListener() {
            @Override
            public void updateCount(ActorId powerupId, int total) {
                switch (powerupId) {
                    case MASK:
                        stats.setMaskCount(total);
                        break;

                    case PAPER:
                        stats.setPaperCount(total);
                        break;
                }
            }
        });
        world.getPlayerActor().setInvincibilityListener(new PlayerActor.InvincibilityListener() {
            @Override
            public void updateTimer(float total) {
                stats.setInvincibilityTime(total);
            }

            @Override
            public void stateChanged(boolean invincible) {
                if (invincible) {
                    currentGameMusic = invincibleMusic;
                    mapMusic.stop();
                    invincibleMusic.play();
                } else {
                    currentGameMusic = mapMusic;
                    invincibleMusic.stop();
                    mapMusic.play();
                }
            }
        });

        addState(States.OPENING, new StateAdapter() {
            private final Transition transition = new HCenterOutTransition(0, 1.3f, 1f);

            @Override
            public void enter() {
                camera.zoom = world.getMaxZoom();
                introMusic.setLooping(false);
                introMusic.play();
            }

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                draw();
                transition.draw();

                if (transition.isComplete()) {
                    setState(States.ZOOMING_IN);
                }
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        addState(States.ZOOMING_IN, new StateAdapter() {
            private final float SPEED = (world.getMaxZoom() - 1)/0.35f;

            @Override
            public void render(float delta) {
                draw();

                if (camera.zoom > 1) {
                    camera.zoom = Math.max(camera.zoom - delta * SPEED, 1);
                } else {
                    setState(States.WAITING_INTRO_MUSIC);
                }
            }
        });

        addState(States.WAITING_INTRO_MUSIC, new StateAdapter() {
            @Override
            public void render(float delta) {
                draw();

                if (!introMusic.isPlaying()) {
                    setState(States.PAUSING_BEFORE_COUNTDOWN);
                }
            }
        });

        addState(States.PAUSING_BEFORE_COUNTDOWN, new StateAdapter() {
            private float waitedTime = 0;

            @Override
            public void render(float delta) {
                draw();

                waitedTime += delta;
                if (waitedTime >= 1f) {
                    setState(States.COUNTDOWN);
                }
            }
        });

        addState(States.COUNTDOWN, new StateAdapter() {
            @Override
            public void enter() {
                countdown.start();
            }

            @Override
            public void render(float delta) {
                countdown.render(delta);

                draw();
                drawCountdown();

                if (!countdown.isCountingDown()) {
                    setState(States.PLAYING);
                }
            }
        });

        addState(States.PLAYING, new StateAdapter() {
            @Override
            public void enter() {
                currentGameMusic.setLooping(true);
                currentGameMusic.play();
                world.setPaused(false);
                stats.setPaused(false);

                Gdx.input.setCatchKey(Input.Keys.BACK, true);
            }

            @Override
            public void leave() {
                currentGameMusic.pause();
                Gdx.input.setInputProcessor(noInput);
            }

            @Override
            public void show() {
                Gdx.input.setInputProcessor(playingInputProcessor);
            }

            @Override
            public void render(float delta) {
                world.render(delta);
                stats.render(delta);
                countdown.render(delta);

                draw();
                drawCountdown();

                if (world.gameIsOver()) {
                    setState(States.GAME_OVER_MUSIC);
                } else if (hardwareWrapper.getGyroscopeY() < Y_GYROSCOPE_PAUSE_TRESHOLD) {
                    setState(States.PAUSED);
                }
            }
        });

        addState(States.PAUSED, new StateAdapter() {
            private boolean pausing;
            private boolean resume;

            @Override
            public void enter() {
                pausing = true;
                resume = false;
                world.setPaused(true);
                stats.setPaused(true);

                pauseSound.play();
            }

            @Override
            public void leave() {
                pauseSound.stop();
                Gdx.input.setInputProcessor(noInput);
            }

            @Override
            public void show() {
                Gdx.input.setInputProcessor(pausedInputProcessor);
            }

            @Override
            public void render(float delta) {
                draw();

                if (pausing) {
                    if (hardwareWrapper.getGyroscopeY() >= Y_GYROSCOPE_PAUSE_TRESHOLD) {
                        pausing = false;
                    }
                } else if (!resume){
                    if (hardwareWrapper.getGyroscopeY() < Y_GYROSCOPE_PAUSE_TRESHOLD) {
                        resume = true;
                    }
                } else if (hardwareWrapper.getGyroscopeY() >= Y_GYROSCOPE_PAUSE_TRESHOLD) {
                    setState(States.PLAYING);
                }
            }
        });

        addState(States.GAME_OVER_MUSIC, new StateAdapter() {
            @Override
            public void enter() {
                currentGameMusic.stop();
                gameOverMusic.setLooping(false);
                gameOverMusic.play();
            }

            @Override
            public void show() {
                Gdx.input.setCatchKey(Input.Keys.BACK, false);
            }

            @Override
            public void render(float delta) {
                draw();

                if (!gameOverMusic.isPlaying()) {
                    setState(States.CLOSING);
                }
            }
        });

        addState(States.CLOSING, new StateAdapter() {
            private final Transition transition = new VCenterInTransition(0, 1.0f, 0.2f);

            @Override
            public void resize(int width, int height) {
                transition.getViewport().update(width, height);
            }

            @Override
            public void render(float delta) {
                transition.render(delta);

                draw();
                transition.draw();

                if (transition.isComplete()) {
                    main.gameOver(GameScreen.this, level, world.getPaperCount(), world.getRunningTime());
                }
            }

            @Override
            public void dispose() {
                transition.dispose();
            }
        });

        setState(States.OPENING);
    }

    /**
     * Dibuja el mundo y las estadísticas en pantalla.
     */
    public void draw() {
        Gdx.gl.glViewport(
                0, Gdx.graphics.getHeight() - STATS_H, Gdx.graphics.getWidth(), STATS_H);
        stats.getStage().draw();
        Gdx.gl.glViewport(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);
        world.getStage().draw();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Dibuja la cuenta atrás en pantalla.
     */
    public void drawCountdown() {
        Gdx.gl.glViewport(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);
        countdown.getStage().draw();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        world.getStage().getViewport().update(width, height);
        stats.getStage().getViewport().update(width, height);
        countdown.getStage().getViewport().update(width, height);

        super.resize(width, height);
    }

    @Override
    public void dispose() {
        world.dispose();
        stats.getStage().dispose();
        countdown.getStage().dispose();

        super.dispose();
    }
}
