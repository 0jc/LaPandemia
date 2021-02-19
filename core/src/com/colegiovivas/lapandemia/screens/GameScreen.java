package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.input.GestureDetector;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.gestures.MovePlayerGestureListener;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import static com.colegiovivas.lapandemia.screens.RectanglesTransition.Dir;

public class GameScreen extends StagedScreen {
    private static final int STATS_H = 75;

    private final int levelId;
    private final LaPandemia parent;
    private final StatsSubscreen statsSubscreen;
    private final WorldSubscreen worldSubscreen;
    private final CountdownSubscreen countdownSubscreen;
    private final RectanglesTransition startTransition;
    private final RectanglesTransition endTransition;

    public GameScreen(LaPandemia parent, int levelId, FileHandle levelFile) {
        super();

        this.parent = parent;
        this.levelId = levelId;

        statsSubscreen = new StatsSubscreen(parent);
        statsSubscreen.setScreenBounds(
                0, Gdx.graphics.getHeight() - STATS_H, Gdx.graphics.getWidth(), STATS_H);
        addSubscreen(statsSubscreen);

        worldSubscreen = new WorldSubscreen(parent, levelFile);
        worldSubscreen.setScreenBounds(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);
        addSubscreen(worldSubscreen);

        countdownSubscreen = new CountdownSubscreen(parent);
        countdownSubscreen.setScreenBounds(worldSubscreen.getScreenBounds());
        addSubscreen(countdownSubscreen);

        startTransition = new RectanglesTransition(400, Dir.OUT, false, 600);
        startTransition.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addSubscreen(startTransition);

        endTransition = new RectanglesTransition(240, Dir.IN, true, 300);
        endTransition.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addSubscreen(endTransition);

        worldSubscreen.getPlayerActor().setPowerupListener(new PlayerActor.PowerupListener() {
            @Override
            public void updateCount(ActorId powerupId, int total) {
                switch (powerupId) {
                    case MASK:
                        statsSubscreen.setMaskCount(total);
                        break;

                    case PAPER:
                        statsSubscreen.setPaperCount(total);
                        break;
                }
            }
        });
        worldSubscreen.getPlayerActor().setInvincibilityListener(new PlayerActor.InvincibilityListener() {
            @Override
            public void updateTimer(float total) {
                statsSubscreen.setInvincibilityTime(total);
            }
        });

        setGameStage(new CountdownGameStage());
    }

    private class PlayingGameStage extends GameStage {
        private final Music music;

        public PlayingGameStage() {
            music = parent.assetManager.get("audio/map.wav");
        }

        @Override
        public void show() {
            InputMultiplexer multiplexer = new InputMultiplexer();
            // ZoomGestureListener debe ir antes de MovePlayerGestureListener, ya que el primero
            // decide quién de los dos debe procesar los gestos tap.
            multiplexer.addProcessor(new GestureDetector(new ZoomGestureListener(worldSubscreen)));
            multiplexer.addProcessor(new GestureDetector(new MovePlayerGestureListener(
                    worldSubscreen.getPlayerActor())));
            Gdx.input.setInputProcessor(multiplexer);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (!music.isPlaying()) {
                music.setLooping(true);
                music.play();
            }

            worldSubscreen.act(delta);
            statsSubscreen.act(delta);
            countdownSubscreen.act(delta);

            statsSubscreen.draw(delta);
            worldSubscreen.draw(delta);
            countdownSubscreen.draw(delta);

            if (worldSubscreen.gameIsOver()) {
                music.stop();
                setGameStage(new EndingGameStage());
            }
        }
    }

    private class CountdownGameStage extends GameStage {
        private static final float ZOOM_PRE_WAIT = 1;
        private static final float ZOOM_POST_WAIT = 1;
        private static final float ZOOM_IN_SPEED = 2.5f;

        private float zoomPreWaitTime;
        private float zoomPostWaitTime;
        private boolean firstFrame = true;
        private boolean countdownStarted = false;
        private boolean allTasksDone = false;

        public CountdownGameStage() {
            zoomPreWaitTime = 0;
            zoomPostWaitTime = 0;
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            OrthographicCamera camera = (OrthographicCamera)worldSubscreen.getWorldStage().getCamera();
            if (firstFrame) {
                firstFrame = false;
                camera.zoom = worldSubscreen.getMaxZoom();
                startTransition.start();
            } else if (startTransition.isPlaying()) {
                startTransition.act(delta);
            } else if (zoomPreWaitTime < ZOOM_PRE_WAIT) {
                zoomPreWaitTime = Math.min(zoomPreWaitTime + delta, ZOOM_PRE_WAIT);
            } else if (camera.zoom > 1) {
                camera.zoom = Math.max(camera.zoom - delta*ZOOM_IN_SPEED, 1f);
            } else if (zoomPostWaitTime < ZOOM_POST_WAIT) {
                zoomPostWaitTime = Math.min(zoomPostWaitTime + delta, ZOOM_POST_WAIT);
            } else if (!countdownStarted) {
                countdownStarted = true;
                countdownSubscreen.startCountdown();
            } else if (countdownSubscreen.isCountingDown()) {
                countdownSubscreen.act(delta);
            } else {
                allTasksDone = true;
            }

            statsSubscreen.draw(delta);
            worldSubscreen.draw(delta);
            countdownSubscreen.draw(delta);
            startTransition.draw(delta);

            if (allTasksDone) {
                setGameStage(new PlayingGameStage());
            }
        }
    }

    private class EndingGameStage extends GameStage {
        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            statsSubscreen.draw(delta);
            worldSubscreen.draw(delta);

            // Se entrará aquí más adelante cuando termine de reproducirse la música
            // del fin de la partida.
            if (true) {
                if (!endTransition.isPlaying()) {
                    endTransition.start();
                }

                endTransition.act(delta);
                endTransition.draw(delta);

                if (!endTransition.isPlaying()) {
                    parent.gameOver(
                            GameScreen.this,
                            levelId,
                            worldSubscreen.getPaperCount(),
                            worldSubscreen.getRunningTime());
                }
            }
        }
    }
}
