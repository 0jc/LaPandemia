package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.input.GestureDetector;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.gestures.MovePlayerGestureListener;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import com.colegiovivas.lapandemia.levels.Level;

public class GameScreen implements Screen {
    private static final int STATS_H = 75;

    private GameStage gameStage;
    private final StatsSubscreen statsSubscreen;
    private final WorldSubscreen worldSubscreen;

    public GameScreen(LaPandemia parent, Level level) {
        statsSubscreen = new StatsSubscreen(parent);
        statsSubscreen.setScreenBounds(
                0, Gdx.graphics.getHeight() - STATS_H, Gdx.graphics.getWidth(), STATS_H);
        worldSubscreen = new WorldSubscreen(parent, level);
        worldSubscreen.setScreenBounds(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);

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

        gameStage = new CountdownGameStage();
    }

    @Override
    public void show() {
        gameStage.show();
    }

    @Override
    public void render(float delta) {
        gameStage.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        statsSubscreen.resize(width, height);
        worldSubscreen.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        statsSubscreen.dispose();
        worldSubscreen.dispose();
    }

    private void nextGameStage(GameStage current) {
        if (current instanceof CountdownGameStage) {
            gameStage = new PlayingGameStage();
        } else if (current instanceof PlayingGameStage) {
            gameStage = new EndingGameStage();
        }

        show();
    }

    private abstract static class GameStage {
        void show() {
            Gdx.input.setInputProcessor(new InputAdapter());
        }

        abstract void render(float delta);
    }

    private class PlayingGameStage extends GameStage {
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

            boolean finish = true;
            if (worldSubscreen.getPlayerActor().isAlive()) {
                finish = false;
                worldSubscreen.act(delta);
            }

            statsSubscreen.draw(delta);
            worldSubscreen.draw(delta);

            if (finish) {
                nextGameStage(this);
            }
        }
    }

    private class CountdownGameStage extends GameStage {
        private static final float ZOOM_PRE_WAIT = 1;
        private static final float ZOOM_POST_WAIT = 0.5f;
        private static final float ZOOM_IN_SPEED = 2.5f;

        private float zoomPreWaitTime;
        private float zoomPostWaitTime;

        public CountdownGameStage() {
            zoomPreWaitTime = 0;
            zoomPostWaitTime = 0;
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            boolean finish = false;
            OrthographicCamera camera = (OrthographicCamera)worldSubscreen.getWorldStage().getCamera();
            if (zoomPreWaitTime < ZOOM_PRE_WAIT) {
                if (zoomPreWaitTime == 0) {
                    camera.zoom = worldSubscreen.getMaxZoom();
                }

                zoomPreWaitTime = Math.min(zoomPreWaitTime + delta, ZOOM_PRE_WAIT);
            } else if (camera.zoom > 1) {
                camera.zoom = Math.max(camera.zoom - delta*ZOOM_IN_SPEED, 1f);
            } else if (zoomPostWaitTime < ZOOM_POST_WAIT) {
                zoomPostWaitTime += delta;
            } else {
                finish = true;
            }

            statsSubscreen.draw(delta);
            worldSubscreen.draw(delta);

            if (finish) {
                nextGameStage(this);
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
        }
    }
}
