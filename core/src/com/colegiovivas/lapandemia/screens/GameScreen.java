package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
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
    private final Group worldGroup;
    private final Group powerups;
    private final Group worldTop;
    private final Group UIGroup;

    public GameScreen(final LaPandemia parent, final Level level) {
        this.parent = parent;
        this.level = level;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(LaPandemia.V_WIDTH, LaPandemia.V_HEIGHT, camera);
        stage = new Stage(viewport);

        worldGroup = new Group();
        UIGroup = new Group();

        powerups = new Group();
        worldTop = new Group();
        worldGroup.addActor(powerups);
        worldGroup.addActor(worldTop);

        collisionDispatcher = new CollisionDispatcher(parent, getWorldGroup());

        playerActor = new PlayerActor(parent, this);
        playerActor.setPosition(level.startX, level.startY);
        playerActor.setCollisionDispatcher(collisionDispatcher);
        playerActor.setDirection(level.startXDir, level.startYDir);
        worldTop.addActor(playerActor);

        HealthActor healthActor = new HealthActor(parent);
        playerActor.setHealthActor(healthActor);
        healthActor.setPlayerActor(playerActor);
        UIGroup.addActor(healthActor);

        for (int i = 0; i < level.fans.size; i++) {
            Fan fan = level.fans.get(i);
            FanActor fanActor = new FanActor(parent);
            fanActor.setPosition(fan.x, fan.y);
            fanActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(fanActor);
        }
        for (int i = 0; i < level.walls.size; i++) {
            Wall wall = level.walls.get(i);
            WallActor wallActor = new WallActor(parent);
            wallActor.setBounds(wall.x, wall.y, wall.w, wall.h);
            wallActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(wallActor);
        }

        ActorGeneratorFactory agf = new ActorGeneratorFactory(this, parent);
        actorGenerators = new Array<>();
        actorGenerators.add(agf.getInstance(VirusActor.class, ActorId.VIRUS, worldTop, 32, 64, 2, 100f, 120f));
        actorGenerators.add(agf.getInstance(MaskActor.class, ActorId.MASK, powerups, 64, 32, 10, 3f, 15f));
        actorGenerators.add(agf.getInstance(PaperActor.class, ActorId.PAPER, powerups, 48, 48, 5, 10f, 15f));
        actorGenerators.add(agf.getInstance(NeedleActor.class, ActorId.NEEDLE, powerups, 22, 64, 60, 1f, 15f));

        stage.addActor(worldGroup);
        stage.addActor(UIGroup);
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

    public Group getWorldGroup() {
        return worldGroup;
    }

    public CollisionDispatcher getCollisionDispatcher() {
        return collisionDispatcher;
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
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
        if (newZoom <= maxZoom()) {
            camera.zoom = newZoom;
        }

        // Si se está ampliando el zoom y tan solo quedan unos pocos píxeles de ampliación
        // para alcanzar el valor máximo, se ajusta automáticamente el zoom al máximo. Si no
        // hiciésemos esto, el jugador tendría que asegurarse de hacer un gesto muy preciso
        // con los dedos para poder establecer este valor exacto y no uno muy ligeramente
        // menor. El problema de estos valores menores es que provocan que el mapa dé un
        // pequeño salto cada vez que el personaje cruza la mitad de la pantalla, lo que tan
        // solo se trata del zoom persiguiéndolo pero suele resultar ser un efecto confuso y
        // molesto.
        if (delta > 0 && (Math.min(level.width - camera.zoom * LaPandemia.V_WIDTH,
                                   level.height - camera.zoom * LaPandemia.V_HEIGHT) < 20))
        {
            camera.zoom = maxZoom();
        }
    }

    private float maxZoom() {
        return Math.min(level.width/LaPandemia.V_WIDTH, level.height/LaPandemia.V_HEIGHT);
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
