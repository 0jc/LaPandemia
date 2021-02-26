package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.screens.RectangleTransition;
import com.colegiovivas.lapandemia.screens.StagedScreen;

public class GameScreen extends StagedScreen {
    private static final int STATS_H = 75;

    static final float Y_GYROSCOPE_PAUSE_TRESHOLD = -6;
    static final int STAGE_OPENING = 0;
    static final int STAGE_WAIT_AFTER_OPENING = 1;
    static final int STAGE_ZOOM_IN = 2;
    static final int STAGE_WAIT_INTRO_MUSIC = 3;
    static final int STAGE_WAIT_AFTER_ZOOM_IN = 4;
    static final int STAGE_COUNTDOWN = 5;
    static final int STAGE_PLAYING = 6;
    static final int STAGE_PAUSE = 7;
    static final int STAGE_GAME_OVER_MUSIC = 8;
    static final int STAGE_CLOSING = 9;

    private final int levelId;
    private final LaPandemia main;
    private final World world;
    private final GameStats stats;
    private final Countdown countdown;
    private final RectangleTransition openingTransition;
    private final RectangleTransition closingTransition;
    private final Music introMusic;
    private final Music gameOverMusic;
    private final Music mapMusic;
    private final Music invincibleMusic;
    private final Sound pauseSound;

    private Music  currentGameMusic;

    public GameScreen(LaPandemia main, int levelId, FileHandle levelFile) {
        this.main = main;
        this.levelId = levelId;

        world = new World(main, levelFile);
        stats = new GameStats(main);
        countdown = new Countdown(main);
        openingTransition = new RectangleTransition(400, RectangleTransition.Dir.OUT, false, 600);
        closingTransition = new RectangleTransition(240, RectangleTransition.Dir.IN, true, 300);

        addGameStage(STAGE_OPENING, new OpeningGameStage(this));
        addGameStage(STAGE_WAIT_AFTER_OPENING, new WaitGameStage(this, 1f, STAGE_ZOOM_IN));
        addGameStage(STAGE_ZOOM_IN, new ZoomInGameStage(this, 2.5f));
        addGameStage(STAGE_WAIT_INTRO_MUSIC, new WaitIntroMusicGameStage(this));
        addGameStage(STAGE_WAIT_AFTER_ZOOM_IN, new WaitGameStage(this, 1f, STAGE_COUNTDOWN));
        addGameStage(STAGE_COUNTDOWN, new CountdownGameStage(main, this));
        addGameStage(STAGE_PLAYING, new PlayingGameStage(main, this));
        addGameStage(STAGE_PAUSE, new PauseGameStage(this));
        addGameStage(STAGE_GAME_OVER_MUSIC, new GameOverMusicGameStage(main, this));
        addGameStage(STAGE_CLOSING, new ClosingGameStage(this));

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

        introMusic = main.assetManager.get("audio/game-opening.wav");
        gameOverMusic = main.assetManager.get("audio/game-over.wav");
        mapMusic = main.assetManager.get("audio/map.wav");
        invincibleMusic = main.assetManager.get("audio/ticking.wav");
        pauseSound = main.assetManager.get("audio/pause.wav");

        currentGameMusic = mapMusic;

        setGameStage(STAGE_OPENING);
    }

    public World getWorld() {
        return world;
    }

    public GameStats getStats() {
        return stats;
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public RectangleTransition getOpeningTransition() {
        return openingTransition;
    }

    public RectangleTransition getClosingTransition() {
        return closingTransition;
    }

    public Music getIntroMusic() {
        return introMusic;
    }

    public Music getGameOverMusic() {
        return gameOverMusic;
    }

    public Music getMapMusic() {
        return mapMusic;
    }

    public Sound getPauseSound() {
        return pauseSound;
    }

    public Music getCurrentGameMusic() {
        return currentGameMusic;
    }

    public void draw() {
        Gdx.gl.glViewport(
                0, Gdx.graphics.getHeight() - STATS_H, Gdx.graphics.getWidth(), STATS_H);
        stats.getStage().draw();
        Gdx.gl.glViewport(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);
        world.getStage().draw();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void drawCountdown() {
        Gdx.gl.glViewport(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - STATS_H);
        countdown.getStage().draw();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void finishGame() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.gameOver(this, levelId, world.getPaperCount(), world.getRunningTime());
    }

    @Override
    public void resize(int width, int height) {
        world.getStage().getViewport().update(width, height);
        stats.getStage().getViewport().update(width, height);
        countdown.getStage().getViewport().update(width, height);
        openingTransition.getViewport().update(width, height);
        closingTransition.getViewport().update(width, height);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        stats.getStage().dispose();
        countdown.getStage().dispose();
        openingTransition.dispose();
        closingTransition.dispose();
    }
}
