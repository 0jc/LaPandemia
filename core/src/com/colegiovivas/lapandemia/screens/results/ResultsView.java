package com.colegiovivas.lapandemia.screens.results;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final LaPandemia main;

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
    private final TextButton continueButton;

    /**
     * Evento que se lanza cuando el usuario pulsa el botón de volver.
     * Configurable mediante setter.
     */
    private ContinueListener continueListener;

    public ResultsView(LaPandemia main, LevelInfo level) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = main.assetManager.get("cloud-form-skin/cloud-form-ui.json");

        levelNameLabel = new Label(level.getName(), cloudFormSkin, "title");
        timeLeftLabel = new Label("Tiempo de juego:", cloudFormSkin);
        timeRightLabel = new Label("", cloudFormSkin);
        paperLeftLabel = new Label("Rollos de papel:", cloudFormSkin);
        paperRightLabel = new Label("", cloudFormSkin);
        continueButton = new TextButton("Volver", cloudFormSkin);
        nickLabel = new Label("Tu nombre:", cloudFormSkin);
        nickField = new TextField("", cloudFormSkin);
        nickField.setText("Profesor Bacterio");

        boolean debug = false;

        // La forma estándar de crear interfaces de usuario en Libgdx es mediante tablas,
        // que se pueden anidar entre ellas.
        Table table = new Table();
        table.setBackground(new MonochromaticDrawable(main, Color.CYAN));
        table.setFillParent(true);
        table.setDebug(debug);
        table.left().top();

        Table levelNameTable = new Table();
        levelNameTable.setBackground(new MonochromaticDrawable(main, Color.MAROON));
        levelNameTable.setDebug(debug);
        table.add(levelNameTable).pad(10).expandX().fillX().row();

        levelNameTable.pad(10);
        levelNameTable.add(levelNameLabel).expandX().left();

        Table statsTable = new Table();
        statsTable.setBackground(new MonochromaticDrawable(main, Color.WHITE));
        statsTable.setDebug(debug);
        table.add(statsTable).padLeft(10).padRight(10).padBottom(10).expand().fill().row();

        statsTable.padLeft(10).padRight(10).padTop(10).padBottom(10).top();
        statsTable.add(timeLeftLabel).expandX().padRight(15).right();
        statsTable.add(timeRightLabel).expandX().left().row();

        statsTable.add(paperLeftLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(paperRightLabel).expandX().left().padTop(10).row();

        statsTable.add(nickLabel).expandX().padRight(15).right().padTop(10);
        statsTable.add(nickField).expandX().left().padTop(10).row();

        table.add(continueButton).expandX().fillX().padLeft(10).padRight(10).padBottom(10).row();

        continueButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                continueClicked();
            }
        });

        stage.addActor(table);

        // Establecemos los valores iniciales a 0. Esto nos permitirá realizar una animación
        // en la que los datos suben gradualmente hasta alcanzar los valores reales.
        setTime(0);
        setPaperCount(0);

        // Ocultamos las diferentes partes del formulario para más adelante ir mostrándolas
        // gradualmente.
        setContinueButtonVisible(false);
        setNicknameVisible(false);
        setPaperCountVisible(false);
        setTimeVisible(false);
    }

    /**
     * Evento que se lanza cuando se hace click en el botón de aceptar.
     */
    private void continueClicked() {
        if (continueListener != null) continueListener.continueClicked();
    }

    /**
     * Establece el color de la banda donde se muestra el nombre del nivel.
     * @param color color de fondo que se establece.
     */
    public void setTitleColor(Color color) {
        ((MonochromaticDrawable)((Table)levelNameLabel.getParent()).getBackground()).setColor(color);
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
     * Cambia la visibilidad del botón de aceptar los resultados.
     * @param visible true para mostrar el botón o false en caso opuesto.
     */
    public void setContinueButtonVisible(boolean visible) {
        continueButton.setVisible(visible);
    }

    /**
     * Cambia el texto del botón de aceptar los resultados.
     * @param text Nuevo texto para el botón.
     */
    public void setContinueButtonText(String text) {
        continueButton.setText(text);
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

    /**
     * Establece un listener para informar de eventos de click en el botón de continuar.
     * @param continueListener Evento que será lanzado.
     */
    public void setContinueListener(ContinueListener continueListener) {
        this.continueListener = continueListener;
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

    public interface ContinueListener {
        void continueClicked();
    }
}
