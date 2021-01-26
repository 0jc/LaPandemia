package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.gameplay.*;
import com.colegiovivas.lapandemia.level.Level;
import com.colegiovivas.lapandemia.pooling.PoolableRectangle;

public class GameScreen implements Screen {
    private static final float SAFE_DISTANCE = 400;

    private final LaPandemia parent;
    private final Level level;
    private final Viewport viewport;
    private final Stage stage;
    private final PlayerActor playerActor;
    private final OrthographicCamera camera;

    private float lastVirusTime;

    public GameScreen(final LaPandemia parent, final Level level) {
        this.parent = parent;
        this.level = level;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(LaPandemia.V_WIDTH, LaPandemia.V_HEIGHT, camera);
        stage = new Stage(viewport);

        playerActor = new PlayerActor(level.startX, level.startY, parent, this);
        playerActor.setDirection(level.startXDir, level.startYDir);
        stage.addActor(playerActor);

        for (int i = 0; i < level.fans.size; i++) {
            stage.addActor(new FanActor(level.fans.get(i), parent));
        }
        for (int i = 0; i < level.walls.size; i++) {
            stage.addActor(new WallActor(level.walls.get(i), parent));
        }

        lastVirusTime = 0;

        // Lo que procede es temporal y para hacer pruebas.
        playerActor.setMaskCount(2);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        // ZoomGestureListener debe ir antes de MovePlayerGestureListener, ya que el primero
        // decide quién de los dos debe procesar los gestos tap.
        multiplexer.addProcessor(new GestureDetector(new ZoomGestureListener(this)));
        multiplexer.addProcessor(new GestureDetector(new MovePlayerGestureListener(playerActor)));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0xFF, 0x88, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int virusCount = 0;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof VirusActor) {
                virusCount++;
            }
        }

        lastVirusTime += delta;
        if (virusCount < level.maxVirusCount && lastVirusTime >= 2) {
            PoolableRectangle rect = parent.rectPool.obtain().init();
            rect.set(0, 0, 32, 64);
            try {
                if (tryAssignCoords(rect)) {
                    Gdx.app.log("LaPandemia", "Assign successful");
                    lastVirusTime = 0;
                    virusCount++;
                    stage.addActor(parent.virusPool.obtain().init(rect.x, rect.y));
                } else {
                    Gdx.app.log("LaPandemia", "Assign successful");
                }
            } finally {
                parent.rectPool.free(rect);
            }
        }

        stage.act();
        // El orden de las siguientes dos líneas es importante. Si se invierten, el movimiento
        // de playerActor se muestra notablemente menos fluido.
        adjustCamera();
        stage.draw();
    }

    public void zoom(float delta) {
        float newZoom = MathUtils.clamp(camera.zoom + delta, 1f, 3f);
        // Multiplicando una distancia en unidades del mundo por el zoom obtenemos su
        // distancia en píxeles de pantalla. Sabiendo esto, nos aseguramos de que el
        // nuevo zoom no sea tan grande que el mapa entero se quede pequeño en alguno
        // de los dos ejes de coordenadas. Esto es necesario para que adjustCamera()
        // pueda averiguar con seguridad y sin tener que manipular el zoom unas
        // coordenadas de la cámara que centren al personaje lo máximo posible sin
        // salir de los límites del mapa.
        if (LaPandemia.V_WIDTH * newZoom <= level.width
                && LaPandemia.V_HEIGHT * newZoom <= level.height) {
            camera.zoom = newZoom;
        }
    }

    private void adjustCamera() {
        // Se muestra siempre el mayor espacio posible alrededor del personaje, pero sin
        // hacer scroll más allá de los límites del mapa. Cuando el personaje no está
        // cerca de los bordes, aparece en el centro de la pantalla.
        float leftBound = LaPandemia.V_WIDTH * camera.zoom / 2;
        float rightBound = level.width - leftBound;
        float lowerBound = camera.zoom * LaPandemia.V_HEIGHT / 2 + 1;
        float upperBound = level.height - lowerBound;
        float xToCenter = playerActor.getX() + playerActor.getWidth() / 2;
        float yToCenter = playerActor.getY() + playerActor.getHeight() / 2;

        camera.position.x = MathUtils.clamp(xToCenter, leftBound, rightBound);
        camera.position.y = MathUtils.clamp(yToCenter, lowerBound, upperBound);
    }

    private boolean tryAssignCoords(Rectangle outCoords) {
        // No generamos coordenadas demasiado cerca de los bordes del mapa, donde de
        // todos modos es improbable que no haya muros en cualquier nivel.
        outCoords.x = MathUtils.random(32, level.width - 32 - 64);
        outCoords.y = MathUtils.random(32, level.height - 32 - 64);

        PoolableRectangle actorRect = parent.rectPool.obtain().init();
        try {
            for (Actor actor : stage.getActors()) {
                if (actor instanceof PlayerActor) {
                    actorRect.x = actor.getX() - SAFE_DISTANCE;
                    actorRect.y = actor.getY() - SAFE_DISTANCE;
                    actorRect.width = 2*SAFE_DISTANCE + actor.getWidth();
                    actorRect.height = 2*SAFE_DISTANCE + actor.getHeight();
                } else {
                    actorRect.x = actor.getX();
                    actorRect.y = actor.getY();
                    actorRect.width = actor.getWidth();
                    actorRect.height = actor.getHeight();
                }

                if (actorRect.overlaps(outCoords)) {
                    return false;
                }
            }
        } finally {
            parent.rectPool.free(actorRect);
        }
        return true;
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
        parent.virusPool.clear();
    }
}
