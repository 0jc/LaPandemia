package com.colegiovivas.lapandemia.screens.mapselection;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.levels.LevelCatalog;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.MonochromaticDrawable;

public class MapSelectionScreen extends ScreenAdapter {
    private final LaPandemia main;
    private final Stage stage;
    private final InputMultiplexer inputProcessor;
    private final InputProcessor noInput;

    public MapSelectionScreen(LaPandemia main, LevelCatalog levelCatalog) {
        this.main = main;

        Camera camera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(400, 240, camera);
        stage = new Stage(viewport);

        AssetManager assetManager = main.getAssetManager();
        Skin cloudFormSkin = assetManager.get("cloud-form-skin/cloud-form-ui.json");
        TextureRegion whitePixel = ((TextureAtlas)assetManager.get("images.pack")).findRegion("ui-whitepixel");

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        outerTable.setBackground(new MonochromaticDrawable(whitePixel, Color.SKY));

        Label headerLabel = new Label("Elige un mapa", cloudFormSkin);
        headerLabel.setFontScaleX(1.5f);
        headerLabel.setFontScaleY(1.5f);

        Table headerTable = new Table();
        headerTable.setBackground(new MonochromaticDrawable(whitePixel, Color.OLIVE));
        headerTable.add(headerLabel).expandX().pad(10).row();

        outerTable.add(headerTable).expandX().fillX().row();

        Table innerTable = new Table();
        innerTable.padLeft(30).padRight(30).padBottom(20);
        int i = 0;
        for (final LevelInfo levelInfo : levelCatalog.levels()) {
            TextButton button = new TextButton(levelInfo.getName(), cloudFormSkin);
            button.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    preview(levelInfo);
                }
            });
            Cell<TextButton> cell = innerTable.add(button).padTop(20).fillX().expandX();
            if (i % 2 == 1) {
                cell.padLeft(15).row();
            } else {
                cell.padRight(15);
            }
            i++;
        }

        ScrollPane scrollPane = new ScrollPane(innerTable);
        scrollPane.setScrollingDisabled(true, false);

        outerTable.add(scrollPane).fill().expand().row();

        stage.addActor(outerTable);

        noInput = new InputAdapter();
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(stage);
        inputProcessor.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    MapSelectionScreen.this.main.navigatedBackToMain(MapSelectionScreen.this);
                    return true;
                }

                return false;
            }
        });
    }

    private void preview(LevelInfo levelInfo) {
        main.previewScreenChosen(this, levelInfo);
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
}
