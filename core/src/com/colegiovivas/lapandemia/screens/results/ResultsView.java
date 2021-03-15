package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

/**
 * Vista del formulario que ocupa toda la pantalla en el que se muestran
 * los resultados de la partida.
 */
public class ResultsView {
    /**
     * Stage de Libgdx que contiene la tabla en la que se organiza la interfaz.
     */
    private final Stage stage;

    /**
     * Etiqueta en la que se muestra el nombre del nivel.
     */
    private final Label levelNameLabel;

    /**
     * Etiqueta que acompaña la duración de la partida.
     */
    private final Label timeLeftLabel;

    /**
     * Etiqueta en la que se muestra la duración de la partida.
     */
    private final Label timeRightLabel;

    /**
     * Etiqueta que acompaña a la puntuación de la partida.
     */
    private final Label paperLeftLabel;

    /**
     * Etiqueta en la que se muestra la puntuación de la partida.
     */
    private final Label paperRightLabel;

    /**
     * Etiqueta que acompaña al nick del jugador.
     */
    private final Label nickLabel;

    /**
     * Campo donde el jugador puede escribir su nick.
     */
    private final TextField nickField;

    /**
     * Botón de aceptar los resultados y volver.
     */
    private final TextButton returnButton;

    /**
     * Botón de volver a jugar otra partida.
     */
    private final TextButton retryButton;

    public ResultsView(AssetManager assetManager, LevelInfo level) {
        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = assetManager.get("cloud-form-skin/cloud-form-ui.json");
        TextureRegion whitePixel = ((TextureAtlas)assetManager.get("images.pack")).findRegion("ui-whitepixel");

        levelNameLabel = new Label(level.getName(), cloudFormSkin, "title");
        timeLeftLabel = new Label("Tiempo de juego:", cloudFormSkin);
        timeRightLabel = new Label("", cloudFormSkin);
        paperLeftLabel = new Label("Rollos de papel:", cloudFormSkin);
        paperRightLabel = new Label("", cloudFormSkin);
        returnButton = new TextButton("Volver", cloudFormSkin);
        retryButton = new TextButton("Repetir", cloudFormSkin);
        nickLabel = new Label("Tu nombre:", cloudFormSkin);
        nickField = new TextField("", cloudFormSkin);
        nickField.setText("Profesor Bacterio");

        boolean debug = false;

        // La forma estándar de crear interfaces de usuario en Libgdx es mediante tablas,
        // que se pueden anidar entre ellas.
        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(whitePixel, Color.CYAN));
        table.setFillParent(true);
        table.setDebug(debug);
        table.left().top();

        Table levelNameTable = new Table();
        levelNameTable.setBackground(new MonochromaticDrawable(whitePixel, Color.MAROON));
        levelNameTable.setDebug(debug);
        table.add(levelNameTable).pad(10).expandX().fillX().row();

        levelNameTable.pad(10);
        levelNameTable.add(levelNameLabel).expandX().left();

        Table statsTable = new Table();
        statsTable.setBackground(new MonochromaticDrawable(whitePixel, Color.WHITE));
        statsTable.setDebug(debug);
        table.add(statsTable).padLeft(10).padRight(10).padBottom(10).expand().fill().row();

        statsTable.padLeft(10).padRight(10).padTop(10).padBottom(10).top();
        statsTable.add(timeLeftLabel).expandX().padRight(15).right();
        statsTable.add(timeRightLabel).expandX().left().row();

        statsTable.add(paperLeftLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(paperRightLabel).expandX().left().padTop(10).row();

        statsTable.add(nickLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(nickField).expandX().left().padTop(10).row();

        Table buttonsTable = new Table();
        buttonsTable.setDebug(debug);
        table.add(buttonsTable).padLeft(10).padRight(10).padBottom(10).expandX().fillX();

        buttonsTable.add(returnButton).expandX().fillX().padRight(10);
        buttonsTable.add(retryButton).expandX().fillX().padLeft(10).row();

        stage.addActor(table);

        // Establecemos los valores iniciales a 0. Esto nos permitirá realizar una animación
        // en la que los datos suben gradualmente hasta alcanzar los valores reales.
        setTime(0);
        setPaperCount(0);

        // Ocultamos las diferentes partes del formulario para más adelante ir mostrándolas
        // gradualmente.
        setReturnButtonVisible(false);
        setRetryButtonVisible(false);
        setNicknameVisible(false);
        setPaperCountVisible(false);
        setTimeVisible(false);
    }

    /**
     * Establece el color de la banda donde se muestra el nombre del nivel.
     * @param color color de fondo que se establece.
     */
    public void setTitleColor(Color color) {
        ((MonochromaticDrawable)((Table)levelNameLabel.getParent()).getBackground()).setNewColor(color);
    }

    /**
     * Cambia la visibilidad de la estadística de la duración de la partida.
     * @param visible true para mostrar la estadística o false en caso opuesto.
     */
    public void setTimeVisible(boolean visible) {
        timeLeftLabel.setVisible(visible);
        timeRightLabel.setVisible(visible);
    }

    /**
     * Cambia la visibilidad de la estadística de la puntuación de la partida.
     * @param visible true para mostrar la estadística o false en caso opuesto.
     */
    public void setPaperCountVisible(boolean visible) {
        paperLeftLabel.setVisible(visible);
        paperRightLabel.setVisible(visible);
    }

    /**
     * Cambia la visibilidad de la fila donde se pide un nick al usuario.
     * @param visible true para mostrar la fila o false en caso opuesto.
     */
    public void setNicknameVisible(boolean visible) {
        nickLabel.setVisible(visible);
        nickField.setVisible(visible);
    }

    /**
     * Cambia la visibilidad del botón de volver.
     * @param visible true para mostrar el botón o false en caso opuesto.
     */
    public void setReturnButtonVisible(boolean visible) {
        returnButton.setVisible(visible);
    }

    /**
     * Cambia la visibilidad del botón de reintentar.
     * @param visible true para mostrar el botón o false en caso opuesto.
     */
    public void setRetryButtonVisible(boolean visible) {
        retryButton.setVisible(visible);
    }

    /**
     * Cambia el tiempo de duración de la partida.
     * @param time Nueva duración en segundos.
     */
    public void setTime(float time) {
        timeRightLabel.setText(String.format("%02d:%02d", (int)time/60, (int)time % 60));
    }

    /**
     * Cambia la puntuación.
     * @param paperCount Nueva puntuación.
     */
    public void setPaperCount(int paperCount) {
        paperRightLabel.setText(paperCount);
    }

    public void addReturnListener(EventListener eventListener) {
        returnButton.addListener(eventListener);
    }

    public void addRetryListener(EventListener eventListener) {
        retryButton.addListener(eventListener);
    }

    /**
     * @return El Stage de Libgdx en el que está contenida la tabla. Es necesario para
     * mostrarla en pantalla, capturar la entrada de datos, etc.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return El nombre introducido por el usuario.
     */
    public String getNickname() {
        return nickField.getText();
    }
}
