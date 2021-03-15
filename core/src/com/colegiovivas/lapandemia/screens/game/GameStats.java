package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.ingameui.GameTimerLabel;
import com.colegiovivas.lapandemia.actors.ingameui.InvincibilityTimerLabel;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

/**
 * Controlador de la información que se muestra en la región superior de la pantalla.
 */
public class GameStats {
    private final Stage stage;

    /**
     * Etiqueta en la que se muestra el número actual de mascarillas.
     */
    private final Label masksLabel;

    /**
     * Etiqueta en la que se muestra el número actual de rollos de papel higiénico.
     */
    private final Label paperLabel;

    /**
     * Etiqueta con la que se indica (en función de si es visible o no) si la partida está
     * pausada.
     */
    private final Label pausedLabel;

    /**
     * Etiqueta que muestra el tiempo que se lleva jugando.
     */
    private final GameTimerLabel runningTimeLabel;

    /**
     * Etiqueta que muestra el tiempo restante para que se acabe el efecto de invencibilidad.
     */
    private final InvincibilityTimerLabel invincibilityTimerLabel;

    /**
     * Si el juego está en pausa o no.
     */
    private boolean paused = false;

    public GameStats(final LaPandemia main) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(800, 40, camera);
        stage = new Stage(viewport);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = main.getAssetManager().get("fonts/nice32.fnt");

        final TextureAtlas atlas = main.getAssetManager().get("images.pack");
        Image maskIcon = new Image(atlas.findRegion("ui-mask"));
        Image paperIcon = new Image(atlas.findRegion("ui-toiletpaper"));
        Image runningTimeIcon = new Image(atlas.findRegion("timer"));
        final Image invincibilityTimeIcon = new Image(atlas.findRegion("invincibility-timer"));

        masksLabel = new Label("0", labelStyle);
        paperLabel = new Label("0", labelStyle);
        pausedLabel = new Label("Pausado", labelStyle);
        runningTimeLabel = new GameTimerLabel(labelStyle);
        invincibilityTimerLabel = new InvincibilityTimerLabel(labelStyle, new InvincibilityTimerLabel.InvincibilityTimerListener() {
            @Override
            public void newValueSet(int value) {
                invincibilityTimeIcon.setVisible(value > 0);
            }
        });

        Table table = new Table();
        table.left();
        table.setFillParent(true);
        table.padRight(20);
        table.padLeft(20);
        table.setBackground(new MonochromaticDrawable(main, Color.BLACK));

        table.add(runningTimeIcon);
        table.add(runningTimeLabel).padLeft(10).padRight(30);
        table.add(invincibilityTimeIcon);
        table.add(invincibilityTimerLabel).padLeft(10);

        Table rightElements = new Table();
        rightElements.add(pausedLabel).padLeft(10).padRight(30);
        rightElements.add(maskIcon);
        rightElements.add(masksLabel).padLeft(10).padRight(30);
        rightElements.add(paperIcon);
        rightElements.add(paperLabel).padLeft(10);
        table.add(rightElements).right().expandX();

        pausedLabel.setVisible(false);

        stage.addActor(table);
    }

    /**
     * Establecer el número de mascarillas que se muestra.
     * @param total Nuevo valor.
     */
    public void setMaskCount(int total) {
        masksLabel.setText(Math.max(total, 0));
    }

    /**
     * Establecer el número de rollos de papel higiénico que se muestra.
     * @param total Nuevo valor.
     */
    public void setPaperCount(int total) {
        paperLabel.setText(Math.max(total, 0));
    }

    /**
     * Establecer el tiempo restante para que se agote el efecto de invencibilidad.
     * @param totalTime Nuevo valor.
     */
    public void setInvincibilityTime(float totalTime) {
        invincibilityTimerLabel.setTimeLeft(totalTime);
    }

    /**
     * @return El Stage al que pertenece la tabla en la que se distribuye la interfaz de usuario.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Establece si el juego está pausado o no, afectando a los contadores de tiempo, al
     * indicador de pausa, etc.
     * @param paused True si el juego está pausado o false en caso contrario.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
        pausedLabel.setVisible(paused);
    }

    /**
     * @return True si y solo si el juego está pausado.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Actualiza el estado del controlador, en caso de no estar pausado.
     * @param delta Segundos transcurridos desde la última actualización.
     */
    public void render(float delta) {
        if (!isPaused()) {
            runningTimeLabel.act(delta);
            invincibilityTimerLabel.act(delta);
        }
    }
}
