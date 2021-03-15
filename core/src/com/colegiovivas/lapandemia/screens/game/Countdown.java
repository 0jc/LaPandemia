package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;

/**
 * Controlador de la cuenta atrás.
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
     * Stage de Libgdx donde se contienen los actores que representan a los números que
     * se muestran en pantalla.
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
    private int countdownNum = -1;

    /**
     * Tiempo que se ha esperado para completar DELAY_AFTER_FADE.
     */
    private float postFadeWaitedTime = 0;

    public Countdown(LaPandemia main) {
        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 480, camera);
        stage = new Stage(viewport);

        camera.position.x = 0;
        camera.position.y = 0;

        images = new Image[4];
        Array<TextureAtlas.AtlasRegion> regions
                = ((TextureAtlas)main.getAssetManager().get("images.pack")).findRegions("countdown");
        for (int i = 0; i <= 3; i++) {
            images[i] = new Image(regions.get(i));
            images[i].setPosition(-images[i].getWidth()/2, -images[i].getHeight()/2);
            images[i].setVisible(false);
            stage.addActor(images[i]);
        }

        numberBeep = main.getAssetManager().get("audio/countdown-beep-number.wav");
        goBeep = main.getAssetManager().get("audio/countdown-beep-go.wav");
    }

    /**
     * Actualizar el estado de la cuenta atrás.
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
     * @return El Stage que contiene los actores que se utilizan para mostrar la cuenta atrás.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return True si y solo si se está reproduciendo la cuenta atrás (incluyendo el "¡YA!").
     */
    public boolean isPlaying() {
        return countdownNum > -1;
    }

    /**
     * @return True si y solo si se está contando hasta 0 (más estricto que isPlaying(),
     * excluyéndose el "¡YA!").
     */
    public boolean isCountingDown() {
        return countdownNum > 0;
    }

    /**
     * Inicia la reproducción de la cuenta atrás.
     */
    public void start() {
        if (countdownNum != -1) {
            resetNum(countdownNum);
        }

        countdownNum = 3;
    }

    /**
     * Establece el número de la cuenta atrás actual, de modo que se reproduce toda su
     * animación. Se incluye el "¡YA!".
     * @param num Índice del actor en images.
     */
    private void setNum(int num) {
        images[num].setVisible(true);
        images[num].addAction(Actions.fadeOut(FADE_OUT_DURATION));
    }

    /**
     * Se restablece el estado de un actor que ha sido mostrado en pantalla durante la
     * cuenta atrás.
     * @param num Índice del actor en images.
     */
    private void resetNum(int num) {
        images[num].setVisible(false);
        images[num].getColor().a = 1;
    }
}
