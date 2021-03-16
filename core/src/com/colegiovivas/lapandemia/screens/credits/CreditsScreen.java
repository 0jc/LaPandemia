package com.colegiovivas.lapandemia.screens.credits;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

public class CreditsScreen extends ScreenAdapter {
    private final LaPandemia main;
    private final Stage stage;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public CreditsScreen(LaPandemia main, AssetManager assetManager) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        Skin cloudFormSkin = assetManager.get("cloud-form-skin/cloud-form-ui.json");
        TextureRegion whitePixel = ((TextureAtlas)assetManager.get("images.pack")).findRegion("ui-whitepixel");

        String topText = "Algunos de los recursos visuales y auditivos presentes en este juego"
                + " han sido producidos por terceras personas bajo una licencia Creative Commons,"
                + " lo que permite su libre uso en aplicaciones como esta. Concretamente, las"
                + " fuentes de las que se han obtenido estos contenidos son las siguientes. El"
                + " componente que se muestra abajo tiene scroll vertical:";
        Label topTextLabel = new Label(topText, cloudFormSkin);
        topTextLabel.setWrap(true);
        topTextLabel.setFontScaleX(+0.8f);
        topTextLabel.setFontScaleY(+0.8f);

        Label creditsTextLabel = new Label(getCreditsText(), cloudFormSkin);
        creditsTextLabel.setWrap(true);
        creditsTextLabel.setFontScale(0.8f);

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        outerTable.setBackground(new MonochromaticDrawable(whitePixel, Color.SKY));
        outerTable.pad(10).top();

        Table headerTable = new Table();
        headerTable.setBackground(new MonochromaticDrawable(whitePixel, Color.SALMON));
        headerTable.pad(10);

        headerTable.add(topTextLabel).expandX().width(360).row();

        outerTable.add(headerTable).expandX().fillX().top().row();

        Table creditsTable = new Table();
        creditsTable.pad(10);

        creditsTable.add(creditsTextLabel).expand().width(360).row();

        ScrollPane scrollPane = new ScrollPane(creditsTable);
        scrollPane.setScrollingDisabled(true, false);

        Table scrollPaneTable = new Table();
        scrollPaneTable.setBackground(new MonochromaticDrawable(whitePixel, Color.CYAN));
        scrollPaneTable.add(scrollPane).center();

        outerTable.add(scrollPaneTable).expand().fill().padTop(10).row();

        stage.addActor(outerTable);

        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(stage);
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    CreditsScreen.this.main.navigatedBackToMain(CreditsScreen.this);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0xFF, 0xFF, 0xFF, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(noInput);
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private String getCreditsText() {
        return "Tema de fondo durante las partidas:\n" +
                "Kevin Macleod - Merry Go\n" +
                "\n" +
                "Pantalla de resultados:\n" +
                "freesound.org/people/BloodPixel/sounds/548653\n" +
                "\n" +
                "Otras pantallas:\n" +
                "freesound.org/people/FoolBoyMedia/sounds/341865\n" +
                "\n" +
                "Invencible:\n" +
                "freesound.org/people/MATRIXXX_/sounds/487724\n" +
                "\n" +
                "Tono de principio de una partida:\n" +
                "freesound.org/people/Mrthenoronha/sounds/518304\n" +
                "\n" +
                "Tono de pausa de una partida:\n" +
                "freesound.org/people/JapanYoshiTheGamer/sounds/361263\n" +
                "\n" +
                "Tres, dos, uno...:\n" +
                "freesound.org/people/JapanYoshiTheGamer/sounds/361254\n" +
                "freesound.org/people/JapanYoshiTheGamer/sounds/361253\n" +
                "\n" +
                "Mascarilla:\n" +
                "freesound.org/people/TreasureSounds/sounds/332629\n" +
                "\n" +
                "Rollo de papel:\n" +
                "freesound.org/people/MATRIXXX_/sounds/402766\n" +
                "\n" +
                "Infectado por un virus:\n" +
                "freesound.org/people/MATRIXXX_/sounds/495541\n" +
                "\n" +
                "Choque con ventilador:\n" +
                "freesound.org/people/MATRIXXX_/sounds/503461\n" +
                "\n" +
                "Choque con muro:\n" +
                "freesound.org/people/Superex1110/sounds/77525\n" +
                "\n" +
                "Fin de la partida:\n" +
                "freesound.org/people/ProjectsU012/sounds/333785\n" +
                "freesound.org/people/EVRetro/sounds/533034\n" +
                "\n" +
                "Virus aniquilado:\n" +
                "freesound.org/people/TheDweebMan/sounds/278164\n" +
                "\n" +
                "Aplausos congratulatorios:\n" +
                "freesound.org/people/modestos1994/sounds/403061\n" +
                "\n" +
                "Resultados finales:\n" +
                "freesound.org/people/Tissman/sounds/534816\n" +
                "freesound.org/people/Fupicat/sounds/538149\n" +
                "\n" +
                "Nivel elegido:\n" +
                "freesound.org/people/JFRecords/sounds/420504\n";
    }
}
