package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.*;
import com.colegiovivas.lapandemia.actors.collision.CollisionDispatcher;
import com.colegiovivas.lapandemia.actors.generator.ActorGenerator;
import com.colegiovivas.lapandemia.actors.generator.ActorGeneratorFactory;
import com.colegiovivas.lapandemia.gestures.MovePlayerGestureListener;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import com.colegiovivas.lapandemia.levels.Fan;
import com.colegiovivas.lapandemia.levels.Level;
import com.colegiovivas.lapandemia.levels.Wall;

public class GameScreen implements Screen {
    private final LaPandemia parent;
    private final Level level;
    private final Viewport viewport;
    private final Stage stage;
    private final PlayerActor playerActor;
    private final OrthographicCamera camera;
    private final Array<ActorGenerator> actorGenerators;
    private final CollisionDispatcher collisionDispatcher;

    public GameScreen(final LaPandemia parent, final Level level) {
        this.parent = parent;
        this.level = level;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(LaPandemia.V_WIDTH, LaPandemia.V_HEIGHT, camera);
        stage = new Stage(viewport);

        collisionDispatcher = new CollisionDispatcher(parent, getStage());
        collisionDispatcher.register(ActorId.PLAYER, PlayerActor.class);
        collisionDispatcher.register(ActorId.WALL, WallActor.class);
        collisionDispatcher.register(ActorId.FAN, FanActor.class);
        collisionDispatcher.register(ActorId.VIRUS, VirusActor.class);
        collisionDispatcher.register(ActorId.MASK, MaskActor.class);
        collisionDispatcher.register(ActorId.PAPER, PaperActor.class);
        collisionDispatcher.register(ActorId.NEEDLE, NeedleActor.class);

        playerActor = new PlayerActor(parent, this);
        playerActor.setPosition(level.startX, level.startY);
        playerActor.setCollisionDispatcher(collisionDispatcher);
        playerActor.setDirection(level.startXDir, level.startYDir);
        stage.addActor(playerActor);

        for (int i = 0; i < level.fans.size; i++) {
            Fan fan = level.fans.get(i);
            FanActor fanActor = new FanActor(parent);
            fanActor.setPosition(fan.x, fan.y);
            fanActor.setCollisionDispatcher(collisionDispatcher);
            stage.addActor(fanActor);
        }
        for (int i = 0; i < level.walls.size; i++) {
            Wall wall = level.walls.get(i);
            WallActor wallActor = new WallActor(parent);
            wallActor.setBounds(wall.x, wall.y, wall.w, wall.h);
            wallActor.setCollisionDispatcher(collisionDispatcher);
            stage.addActor(wallActor);
        }

        ActorGeneratorFactory agf = new ActorGeneratorFactory(this, parent);
        actorGenerators = new Array<>();
        actorGenerators.add(agf.getInstance(2, 32, 64, 100f, 120f, new Pool<GenerableActor>() {
            @Override
            protected VirusActor newObject() {
                return new VirusActor(parent);
            }
        }));
        actorGenerators.add(agf.getInstance(10, 64, 32, 3f, 15f, new Pool<GenerableActor>() {
            @Override
            protected MaskActor newObject() {
                return new MaskActor(parent);
            }
        }));
        actorGenerators.add(agf.getInstance(5, 48, 48, 10f, 15f, new Pool<GenerableActor>() {
            @Override
            protected PaperActor newObject() {
                return new PaperActor(parent);
            }
        }));
        actorGenerators.add(agf.getInstance(60, 22, 64, 1f, 15f, new Pool<GenerableActor>() {
            @Override
            protected NeedleActor newObject() {
                return new NeedleActor(parent);
            }
        }));
    }

    public Stage getStage() {
        return stage;
    }

    public int getWorldWidth() {
        return level.width;
    }

    public int getWorldHeight() {
        return level.height;
    }

    public CollisionDispatcher getCollisionDispatcher() {
        return collisionDispatcher;
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

        for (int i = 0; i < actorGenerators.size; i++) {
            actorGenerators.get(i).render(delta);
        }

        stage.act();
        // El orden de las siguientes dos líneas es importante. Si se invierten, el movimiento
        // de playerActor se muestra notablemente menos fluido.
        adjustCamera();
        stage.draw();
    }

    public void zoom(float delta) {
        //float newZoom = MathUtils.clamp(camera.zoom + delta, 0.8f, 1.6f);
        float newZoom = Math.max(camera.zoom + delta, 0.8f);
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
        float leftBound = camera.zoom * LaPandemia.V_WIDTH / 2;
        float rightBound = level.width - leftBound;
        float lowerBound = camera.zoom * LaPandemia.V_HEIGHT / 2 + 1;
        float upperBound = level.height - lowerBound;
        float xToCenter = playerActor.getX() + playerActor.getWidth() / 2;
        float yToCenter = playerActor.getY() + playerActor.getHeight() / 2;

        camera.position.x = MathUtils.clamp(xToCenter, leftBound, rightBound);
        camera.position.y = MathUtils.clamp(yToCenter, lowerBound, upperBound);
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
        for (ActorGenerator actorGenerator : actorGenerators) {
            actorGenerator.getPool().clear();
        }
    }

}
