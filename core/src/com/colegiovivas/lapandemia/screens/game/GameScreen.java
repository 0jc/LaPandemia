package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.ActorId;
import com.colegiovivas.lapandemia.actors.world.PlayerActor;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MultistateScreen;
import com.colegiovivas.lapandemia.screens.transitions.HCenterOutTransition;
import com.colegiovivas.lapandemia.screens.transitions.VCenterInTransition;

/**
 * Pantalla de juego. Se muestra en ella un mapa cargado junto a una pequeña
 * interfaz en el borde superior con estadísticas de la partida y otra información
 * relevante para el usuario.
 */
public class GameScreen extends MultistateScreen {
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

    static final int STAGE_OPENING = 1;
    static final int STAGE_ZOOM_IN = 2;
    static final int STAGE_WAIT_INTRO_MUSIC = 3;
    static final int STAGE_WAIT_AFTER_ZOOM_IN = 4;
    static final int STAGE_COUNTDOWN = 5;
    static final int STAGE_PLAYING = 6;
    static final int STAGE_PAUSE = 7;
    static final int STAGE_GAME_OVER_MUSIC = 8;
    static final int STAGE_CLOSING = 9;

    /**
     * Nivel en el que se desarrolla la partida.
     */
    private final LevelInfo level;

    /**
     * Clase principal de la aplicación.
     */
    private final LaPandemia main;

    /**
     * Controlador del mundo de la partida.
     */
    private final World world;

    /**
     * Controlador de las estadísticas de la partida.
     */
    private final GameStats stats;

    /**
     * Controlador de la cuenta atrás de la partida.
     */
    private final Countdown countdown;

    /**
     * Transición inicial.
     */
    private final HCenterOutTransition openingTransition;

    /**
     * Transición final.
     */
    private final VCenterInTransition closingTransition;

    /**
     * Música introductoria.
     */
    private final Music introMusic;

    /**
     * Música que se reproduce al final de la partida.
     */
    private final Music gameOverMusic;

    /**
     * Música que se reproduce durante la partida.
     */
    private final Music mapMusic;

    /**
     * Música que se reproduce mientras el jugador es invencible.
     */
    private final Music invincibleMusic;

    /**
     * Sonido que se reproduce cuando se entra en el modo de pausa.
     */
    private final Sound pauseSound;

    /**
     * Música que se está reproduciendo actualmente durante la partida
     * (la habitual de mapa o la de invencibilidad).
     */
    private Music  currentGameMusic;

    public GameScreen(LaPandemia main, LevelInfo level) {
        this.main = main;
        this.level = level;

        world = new World(main, level);
        stats = new GameStats(main);
        countdown = new Countdown(main);
        openingTransition = new HCenterOutTransition(0, 1.3f, 1f);
        closingTransition = new VCenterInTransition(0, 1.0f, 0.2f);

        addState(STAGE_OPENING, new OpeningState(this));
        addState(STAGE_ZOOM_IN, new ZoomInState(this, 2.5f));
        addState(STAGE_WAIT_INTRO_MUSIC, new WaitIntroMusicState(this));
        addState(STAGE_WAIT_AFTER_ZOOM_IN, new WaitState(this, 1f, STAGE_COUNTDOWN));
        addState(STAGE_COUNTDOWN, new CountdownState(main, this));
        addState(STAGE_PLAYING, new PlayingState(main, this));
        addState(STAGE_PAUSE, new PauseState(this));
        addState(STAGE_GAME_OVER_MUSIC, new GameOverMusicState(main, this));
        addState(STAGE_CLOSING, new ClosingState(this));

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

        setState(STAGE_OPENING);
    }

    /**
     * @return Controlador del mundo.
     */
    public World getWorld() {
        return world;
    }

    /**
     * @return Controlador de las estadísticas.
     */
    public GameStats getStats() {
        return stats;
    }

    /**
     * @return Controlador de la cuenta atrás.
     */
    public Countdown getCountdown() {
        return countdown;
    }

    /**
     * @return Transición inicial.
     */
    public HCenterOutTransition getOpeningTransition() {
        return openingTransition;
    }

    /**
     * @return Transición final.
     */
    public VCenterInTransition getClosingTransition() {
        return closingTransition;
    }

    /**
     * @return Música introductoria.
     */
    public Music getIntroMusic() {
        return introMusic;
    }

    /**
     * @return Música que se reproduce al final de la partida.
     */
    public Music getGameOverMusic() {
        return gameOverMusic;
    }

    /**
     * @return Música que se reproduce durante la partida.
     */
    public Music getMapMusic() {
        return mapMusic;
    }

    /**
     * @return Sonido que se reproduce al pausar la partida.
     */
    public Sound getPauseSound() {
        return pauseSound;
    }

    /**
     * @return Música que se está reproduciendo actualmente en la partida.
     */
    public Music getCurrentGameMusic() {
        return currentGameMusic;
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

    /**
     * Transfiere el control de vuelta a la clase principal.
     */
    public void finishGame() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.gameOver(this, level, world.getPaperCount(), world.getRunningTime());
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
