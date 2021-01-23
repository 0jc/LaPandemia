package com.example.testgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.testgame.MyTestGame;
import com.example.testgame.gameplay.MovePlayerGestureListener;
import com.example.testgame.gameplay.PlayerActor;
import com.example.testgame.gameplay.FanActor;
import com.example.testgame.gameplay.WallActor;
import com.example.testgame.level.Level;

public class GameScreen implements Screen {
    private final MyTestGame parent;
    private final Level level;
    private final Viewport viewport;
    private final Stage stage;
    private final PlayerActor playerActor;

    public GameScreen(final MyTestGame parent, final Level level) {
        this.parent = parent;
        this.level = level;

        viewport = new ExtendViewport(MyTestGame.V_WIDTH, MyTestGame.V_HEIGHT);
        stage = new Stage(viewport);

        playerActor = new PlayerActor(level.startX, level.startY, parent);
        playerActor.setDirection(level.startXDir, level.startYDir);
        stage.addActor(playerActor);

        for (int i = 0; i < level.fans.size; i++) {
            stage.addActor(new FanActor(level.fans.get(i), parent));
        }
        for (int i = 0; i < level.walls.size; i++) {
            stage.addActor(new WallActor(level.walls.get(i), parent));
        }
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new MovePlayerGestureListener(playerActor)));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        // El orden de las siguientes dos líneas es importante. Si se invierten, el movimiento
        // de playerActor se muestra notablemente menos fluido.
        adjustCamera();
        stage.draw();
    }

    private void adjustCamera() {
        // Se muestra siempre el mayor espacio posible alrededor del personaje, pero sin
        // hacer scroll más allá de los límites del mapa. Cuando el personaje no está
        // cerca de los bordes, aparece en el centro de la pantalla.
        float leftBound = MyTestGame.V_WIDTH/2;
        float rightBound = level.width - MyTestGame.V_WIDTH/2;
        float lowerBound = MyTestGame.V_HEIGHT/2 + 1;
        float upperBound = level.height - MyTestGame.V_HEIGHT/2 - 1;
        float xToCenter = playerActor.getX() + playerActor.getWidth()/2;
        float yToCenter = playerActor.getY() + playerActor.getHeight()/2;

        stage.getCamera().position.x = Math.min(rightBound, Math.max(leftBound, xToCenter));
        stage.getCamera().position.y = Math.min(upperBound, Math.max(lowerBound, yToCenter));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
