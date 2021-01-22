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

public class GameScreen implements Screen {
    private final MyTestGame parent;
    private final Viewport viewport;
    private final Stage stage;
    private final PlayerActor playerActor;

    // Una vez introducido el sistema de carga de mapas, estas constantes no serán
    // necesarias, ni será necesario tampoco que los actores trabajen con ellas.
    // Por el momento las he introducido para hacer más fácil hacer pruebas.
    // Indican las dimensiones del mapa. Las coordenadas válidas son de (0, 0) a
    // (WORLD_W, WORLD_H). Las coordenadas inválidas no solo son inaccesibles para
    // los actores sino que además no se muestran al hacer scroll cerca de los bordes
    // del mapa.
    private final float WORLD_W = 1600;
    private final float WORLD_H = 960;

    public GameScreen(final MyTestGame parent) {
        this.parent = parent;

        viewport = new ExtendViewport(MyTestGame.V_WIDTH, MyTestGame.V_HEIGHT);
        stage = new Stage(viewport);

        // Como ya se ha dicho, WORLD_W y WORLD_H se dejarán de pasar más adelante.
        // Además de eso, las coordenadas x e y iniciales vendrán indicadas en el mapa
        // que se esté cargando, pero por ahora solo las ajustaremos al centro del mapa.
        playerActor = new PlayerActor(WORLD_W/2, WORLD_H/2, parent.assetManager, WORLD_W, WORLD_H);
        stage.addActor(playerActor);

        // Este actor solo está aquí temporalmente para demostrar el efecto scroll en un
        // mapa que de otro modo estaría vacío. En pantalla, se corresponde con el personaje
        // que parpadea de verde, y siempre está quieto; el que se mueve es el de rojo!
        PlayerActor dummy = new PlayerActor(WORLD_W/2 - 128, WORLD_H/2 - 128, parent.assetManager,
                WORLD_W, WORLD_H);
        dummy.setMaskCount(15); // Para que parpadee de verde y lo distingamos bien del otro.
        stage.addActor(dummy);
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
        float rightBound = WORLD_W - MyTestGame.V_WIDTH/2;
        float lowerBound = MyTestGame.V_HEIGHT/2;
        float upperBound = WORLD_H - MyTestGame.V_HEIGHT/2;
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
