package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.colegiovivas.lapandemia.LaPandemia;
import com.colegiovivas.lapandemia.actors.world.*;
import com.colegiovivas.lapandemia.actors.world.collision.CollisionDispatcher;
import com.colegiovivas.lapandemia.actors.world.generator.ActorGenerator;
import com.colegiovivas.lapandemia.gestures.ZoomGestureListener;
import com.colegiovivas.lapandemia.level.Level;
import com.colegiovivas.lapandemia.level.LevelFanActor;
import com.colegiovivas.lapandemia.level.LevelWallActor;

public class World implements ZoomGestureListener.ZoomListener {
    private final WorldStage stage;
    private PlayerActor playerActor;
    private Array<ActorGenerator> actorGenerators;
    private final CollisionDispatcher collisionDispatcher;
    private final Group rootGroup;
    private int width;
    private int height;
    private float maxZoom;
    private float runningTime;
    private boolean paused = false;

    public World(LaPandemia main, FileHandle levelFile) {
        OrthographicCamera worldCamera = new OrthographicCamera();
        Viewport worldViewport = new StretchViewport(800, 480, worldCamera);
        stage = new WorldStage(worldViewport);

        rootGroup = new Group();
        stage.addActor(rootGroup);

        collisionDispatcher = new CollisionDispatcher(main, rootGroup);

        setUpMap(main, levelFile);

        HealthActor healthActor = new HealthActor(main);
        healthActor.setWorld(this);
        playerActor.setHealthActor(healthActor);
        healthActor.setPlayerActor(playerActor);
        Group healthGroup = new Group();
        healthGroup.addActor(healthActor);
        stage.addActor(healthGroup);
    }

    private void setUpMap(LaPandemia main, FileHandle levelFile) {
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(false);
        json.setOutputType(JsonWriter.OutputType.json);

        Level level = json.fromJson(Level.class, levelFile);

        width = level.size[0];
        height = level.size[1];

        Group powerups = new Group();
        Group worldTop = new Group();
        rootGroup.addActor(powerups);
        rootGroup.addActor(worldTop);

        playerActor = new PlayerActor(main);
        playerActor.setWorld(this);
        playerActor.setPosition(level.playerState.pos[0], level.playerState.pos[1]);
        playerActor.setCollisionDispatcher(collisionDispatcher);
        playerActor.setDirection(level.playerState.dir[0], level.playerState.dir[1]);
        worldTop.addActor(playerActor);

        for (LevelFanActor levelFanActor : level.fans) {
            FanActor fanActor = new FanActor(main, levelFanActor.sprite, levelFanActor.frameDuration);
            fanActor.setWorld(this);
            fanActor.setPosition(levelFanActor.pos[0], levelFanActor.pos[1]);
            fanActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(fanActor);
        }
        for (LevelWallActor levelWallActor : level.walls) {
            WallActor wallActor = new WallActor(main, levelWallActor.sprite);
            wallActor.setWorld(this);
            wallActor.setPosition(levelWallActor.pos[0], levelWallActor.pos[1]);
            wallActor.setSize(levelWallActor.size[0], levelWallActor.size[1]);
            wallActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(wallActor);
        }

        actorGenerators = new Array<>();
        actorGenerators.add(new ActorGenerator(VirusActor.class, ActorId.VIRUS, worldTop, 48, 48, 1.25f, 1000, 120f, main, this));
        actorGenerators.add(new ActorGenerator(MaskActor.class, ActorId.MASK, powerups, 48, 48, 10, 3, 30f, main, this));
        actorGenerators.add(new ActorGenerator(PaperActor.class, ActorId.PAPER, powerups, 48, 48, 5, 10, 30f, main, this));
        actorGenerators.add(new ActorGenerator(NeedleActor.class, ActorId.NEEDLE, powerups, 22, 64, 90, 1, 30f, main, this));

        maxZoom = Math.min(width /stage.getViewport().getWorldWidth(),
                height /stage.getViewport().getWorldHeight());
    }

    public void render(float delta) {
        if (!isPaused()) {
            runningTime += delta;

            for (int i = 0; i < actorGenerators.size; i++) {
                actorGenerators.get(i).render(delta);
            }

            stage.act();
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public Stage getStage() {
        return stage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public Group getRootGroup() {
        return rootGroup;
    }

    public CollisionDispatcher getCollisionDispatcher() {
        return collisionDispatcher;
    }

    public PlayerActor getPlayerActor() {
        return playerActor;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public int getPaperCount() {
        return playerActor.getPaperCount();
    }

    public float getRunningTime() {
        return runningTime;
    }

    public boolean gameIsOver() {
        return !playerActor.isAlive();
    }

    @Override
    public void zoom(float delta) {
        // Nos aseguramos de que el nuevo zoom no sea tan grande que el mapa entero se
        // quede pequeño en alguno de los dos ejes de coordenadas. Esto es necesario para
        // que adjustCamera() pueda averiguar con seguridad y sin tener que manipular el
        // zoom unas coordenadas de la cámara que centren al personaje lo máximo posible
        // sin mostrar aquello que hay más allá de los límites del mapa.
        OrthographicCamera camera = (OrthographicCamera)stage.getCamera();
        camera.zoom = MathUtils.clamp(camera.zoom + delta, 0.8f, maxZoom);

        // Si se está ampliando el zoom y tan solo quedan unos pocos píxeles de ampliación
        // para alcanzar el valor máximo, se ajusta automáticamente el zoom al máximo. Si no
        // hiciésemos esto, el jugador tendría que asegurarse de hacer un gesto muy preciso
        // con los dedos para poder establecer este valor exacto y no uno muy ligeramente
        // menor. El problema de estos valores menores es que provocan que el mapa dé un
        // pequeño salto cada vez que el personaje cruza la mitad de la pantalla, lo que tan
        // solo se trata del zoom persiguiéndolo pero suele resultar ser un efecto confuso y
        // molesto.
        if (delta > 0 && (Math.min(width - camera.zoom * stage.getViewport().getWorldWidth(),
                height - camera.zoom * stage.getViewport().getWorldHeight()) < 20))
        {
            camera.zoom = maxZoom;
        }
    }

    private class WorldStage extends Stage {
        private final OrthographicCamera camera;

        public WorldStage(Viewport viewport) {
            super(viewport);
            camera = (OrthographicCamera)getCamera();
        }

        @Override
        public void draw() {
            adjustCamera();
            super.draw();
        }

        private void adjustCamera() {
            // Se muestra siempre el mayor espacio posible alrededor del personaje, pero sin
            // hacer scroll más allá de los límites del mapa. Cuando el personaje no está
            // cerca de los bordes, aparece en el centro de la pantalla.
            float leftBound = camera.zoom * getViewport().getWorldWidth() / 2;
            float rightBound = width - leftBound;
            float lowerBound = camera.zoom * getViewport().getWorldHeight() / 2 + 1;
            float upperBound = height - lowerBound;
            float xToCenter = playerActor.getX() + playerActor.getWidth() / 2;
            float yToCenter = playerActor.getY() + playerActor.getHeight() / 2;

            camera.position.x = MathUtils.clamp(xToCenter, leftBound, rightBound);
            camera.position.y = MathUtils.clamp(yToCenter, lowerBound, upperBound);
        }
    }
}
