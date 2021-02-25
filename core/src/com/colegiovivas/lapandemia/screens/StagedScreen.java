package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.Iterator;

public abstract class StagedScreen implements Screen {
    private final ArrayMap<Integer, GameStage> gameStages;
    private Integer currentGameStageId;

    public StagedScreen() {
        gameStages = new ArrayMap<>();
    }

    @Override
    public void render(float delta) {
        getCurrentGameStage().render(delta);
    }

    @Override
    public void resize(int width, int height) {
        getCurrentGameStage().resize(width, height);
    }

    @Override
    public void pause() {
        getCurrentGameStage().pause();
    }

    @Override
    public void resume() {
        getCurrentGameStage().resume();
    }

    @Override
    public void hide() {
        if (currentGameStageId != null) getCurrentGameStage().hide();
    }

    @Override
    public void show() {
        getCurrentGameStage().show();
    }

    public void setGameStage(int stageId) {
        if (!gameStages.containsKey(stageId)) {
            throw new IllegalArgumentException();
        }

        GameStage oldGameStage = getCurrentGameStage();
        if (oldGameStage != null) {
            oldGameStage.leave();
        }

        currentGameStageId = stageId;
        gameStages.get(currentGameStageId).enter();
        gameStages.get(currentGameStageId).show();
    }

    public void addGameStage(int id, GameStage stage) {
        if (gameStages.containsKey(id) || stage == null) {
            throw new IllegalArgumentException();
        }

        gameStages.put(id, stage);
    }

    @Override
    public void dispose() {
        Iterator<GameStage> iterator = gameStages.values();
        while (iterator.hasNext()) {
            GameStage stage = iterator.next();
            stage.leave();
            stage.dispose();
        }

        gameStages.clear();
        currentGameStageId = null;
    }

    public GameStage getCurrentGameStage() {
        if (currentGameStageId != null) {
            return gameStages.get(currentGameStageId);
        } else {
            return null;
        }
    }

    public interface GameStage extends Screen {
        void enter();
        void leave();
    }
}
