package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Vista de la cuenta atrás.
 */
public class Countdown {
    /**
     * Pausa que se realiza después de desaparecer un número antes de proceder al siguiente.
     */
    private static final float DELAY_AFTER_FADE = 0.2f;

    /**
     * Tiempo que le lleva a un número desaparecer completamente desde su aparición inicial.
     */
    private static final float FADE_OUT_DURATION = 1.0f;

    /**
     * Stage que contiene los actores para los números que se van mostrando en pantalla.
     */
    private final Stage stage;

    /**
     * Actores imagen que se muestran durante la cuenta atrás (en orden: "¡YA!", "1", "2", "3").
     */
    private final Image[] images;

    /**
     * Sonido que se reproduce junto con un número.
     */
    private final Sound numberBeep;

    /**
     * Sonido que se reproduce junto al "¡YA!".
     */
    private final Sound goBeep;

    /**
     * Número actual que está siendo mostrado o -1 si la cuenta atrás no está en reproducción.
     */
    private int countdownNum = 3;

    /**
     * Tiempo que se ha esperado para completar {@link #DELAY_AFTER_FADE}.
     */
    private float postFadeWaitedTime = 0;

    /**
     * Inicializa la vista.
     * @param assetManager Gestor de recursos de la aplicación, necesario para la renderización de la vista.
     */
    public Countdown(AssetManager assetManager) {
        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 480, camera);
        stage = new Stage(viewport);

        camera.position.x = 0;
        camera.position.y = 0;

        images = new Image[4];
        Array<TextureAtlas.AtlasRegion> regions
                = ((TextureAtlas)assetManager.get("images.pack")).findRegions("countdown");
        for (int i = 0; i <= 3; i++) {
            images[i] = new Image(regions.get(i));
            images[i].setPosition(-images[i].getWidth()/2, -images[i].getHeight()/2);
            images[i].setVisible(false);
            stage.addActor(images[i]);
        }

        numberBeep = assetManager.get("audio/countdown-beep-number.wav");
        goBeep = assetManager.get("audio/countdown-beep-go.wav");
    }

    /**
     * Actualiza el estado de la cuenta atrás.
     * @param delta Segundos que han transcurrido desde la última actualización.
     */
    public void render(float delta) {
        if (countdownNum > -1) {
            float currentAlpha = images[countdownNum].getColor().a;
            if (currentAlpha == 1) {
                if (countdownNum > 0) {
                    numberBeep.play();
                } else {
                    goBeep.play();
                }
                images[countdownNum].setVisible(true);
                images[countdownNum].addAction(Actions.fadeOut(FADE_OUT_DURATION));
            } else if (currentAlpha == 0) {
                postFadeWaitedTime += delta;
                if (postFadeWaitedTime >= DELAY_AFTER_FADE) {
                    images[countdownNum].setVisible(false);
                    images[countdownNum].getColor().a = 1;
                    countdownNum--;
                    postFadeWaitedTime = 0;
                }
            }
            stage.act();
        }
    }

    /**
     * @return {@link #stage}
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return True si y solo si se está contando hasta 0, no incluido (es decir,
     * excluyéndose el "¡YA!").
     */
    public boolean isCountingDown() {
        return countdownNum > 0;
    }
}
