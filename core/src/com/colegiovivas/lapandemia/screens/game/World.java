package com.colegiovivas.lapandemia.screens.game;

import com.badlogic.gdx.assets.AssetManager;
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
import com.colegiovivas.lapandemia.hardware.HardwareWrapper;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.levels.json.LevelJson;
import com.colegiovivas.lapandemia.levels.json.FanActorJsonEntry;
import com.colegiovivas.lapandemia.levels.json.WallActorJsonEntry;

/**
 * Controlador del mundo en el que se desarrolla la partida.
 */
public class World implements ZoomGestureListener.ZoomListener {
    /**
     * Stage de los actores (muros, virus, el personaje principal, etc.).
     */
    private final WorldStage stage;

    /**
     * Personaje principal, controlado por el jugador.
     */
    private PlayerActor playerActor;

    /**
     * Generadores de actores. Añaden actores de un tipo específico al mapa
     * periódicamente, escogiendo siempre lugares que no estén ocupados por
     * actores que no acepten la colisión resultante ni estén demasiado cerca
     * del personaje principal.
     */
    private Array<ActorGenerator> actorGenerators;

    /**
     * Gestor de colisiones. Los actores le informan de los desplazamientos que
     * quieren realizar y el gestor se encarga de ajustar estos desplazamientos
     * cuando sea necesario para asegurarse de que las nuevas coordenadas son
     * válidas para el actor y de lanzar eventos de colisión a los actores
     * afectados.
     */
    private final CollisionDispatcher collisionDispatcher;

    /**
     * Grupo que contiene todos los actores pertenecientes al mundo. Dentro de
     * la región de pantalla destinada a mostrar el mundo, el único actor que
     * se excluye de este grupo es el indicador de salud, de forma que se muestra
     * por encima de todos los demás actores y no es colisionable.
     */
    private final Group rootGroup;

    /**
     * Anchura del mapa.
     */
    private int width;

    /**
     * Altura del mapa.
     */
    private int height;

    /**
     * Zoom máximo que se puede aplicar en el mapa. Cuanto más alto es el valor,
     * mayor es la proporción del mapa que se puede mostrar y más pequeños se ven
     * los actores. Este valor máximo asegura que, sea cual sea la posición del
     * personaje principal, exista una posición de la cámara donde se muestre el
     * personaje sin hacer visibles regiones del mapa que estén fuera de límites.
     */
    private float maxZoom;

    /**
     * Tiempo actual de duración de la partida.
     */
    private float runningTime;

    /**
     * Si la partida está o no pausada.
     */
    private boolean paused = false;

    /**
     * Inicializa el mundo.
     * @param assetManager Gestor de recursos de la aplicación.
     * @param hardwareWrapper Interfaz hacia los sensores hardware tal y como los ha configurado el usuario.
     * @param level Nivel en el que se desarrolla la partida.
     */
    public World(AssetManager assetManager, HardwareWrapper hardwareWrapper, LevelInfo level) {
        OrthographicCamera worldCamera = new OrthographicCamera();
        Viewport worldViewport = new StretchViewport(800, 480, worldCamera);
        stage = new WorldStage(worldViewport);

        rootGroup = new Group();
        stage.addActor(rootGroup);

        collisionDispatcher = new CollisionDispatcher(rootGroup);

        setUpMap(assetManager, hardwareWrapper, level);

        HealthActor healthActor = new HealthActor(assetManager);
        healthActor.setWorld(this);
        playerActor.setHealthActor(healthActor);
        healthActor.setPlayerActor(playerActor);
        Group healthGroup = new Group();
        healthGroup.addActor(healthActor);
        stage.addActor(healthGroup);
    }

    /**
     * Carga un mapa de juego, añadiendo actores, calculando parámetros como el zoom
     * máximo y creando los generadores de actores.
     * @param assetManager Gestor de recursos de la aplicación.
     * @param hardwareWrapper Interfaz hacia los sensores hardware tal y como los ha configurado el usuario.
     * @param level Nivel en el que se desarrolla la partida.
     */
    private void setUpMap(AssetManager assetManager, HardwareWrapper hardwareWrapper, LevelInfo level) {
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(false);
        json.setOutputType(JsonWriter.OutputType.json);

        LevelJson levelJson = json.fromJson(LevelJson.class, level.getLevelJsonFile());

        width = levelJson.size[0];
        height = levelJson.size[1];

        Group powerups = new Group();
        Group worldTop = new Group();
        rootGroup.addActor(powerups);
        rootGroup.addActor(worldTop);

        playerActor = new PlayerActor(assetManager, hardwareWrapper);
        playerActor.setWorld(this);
        playerActor.setPosition(levelJson.playerState.pos[0], levelJson.playerState.pos[1]);
        playerActor.setCollisionDispatcher(collisionDispatcher);
        playerActor.setDirection(levelJson.playerState.dir[0], levelJson.playerState.dir[1]);
        worldTop.addActor(playerActor);

        for (FanActorJsonEntry fanActorJsonEntry : levelJson.fans) {
            FanActor fanActor = new FanActor(assetManager, fanActorJsonEntry.sprite,
                    fanActorJsonEntry.frameDuration);
            fanActor.setWorld(this);
            fanActor.setPosition(fanActorJsonEntry.pos[0], fanActorJsonEntry.pos[1]);
            fanActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(fanActor);
        }
        for (WallActorJsonEntry wallActorJsonEntry : levelJson.walls) {
            WallActor wallActor = new WallActor(assetManager, wallActorJsonEntry.sprite);
            wallActor.setWorld(this);
            wallActor.setPosition(wallActorJsonEntry.pos[0], wallActorJsonEntry.pos[1]);
            wallActor.setSize(wallActorJsonEntry.size[0], wallActorJsonEntry.size[1]);
            wallActor.setCollisionDispatcher(collisionDispatcher);
            worldTop.addActor(wallActor);
        }

        actorGenerators = new Array<>();
        actorGenerators.add(new ActorGenerator(VirusActor.class, ActorId.VIRUS, worldTop, 48, 48, 1.25f, 1000, 120f, assetManager, this));
        actorGenerators.add(new ActorGenerator(MaskActor.class, ActorId.MASK, powerups, 48, 48, 10, 3, 30f, assetManager, this));
        actorGenerators.add(new ActorGenerator(PaperActor.class, ActorId.PAPER, powerups, 48, 48, 5, 10, 30f, assetManager, this));
        actorGenerators.add(new ActorGenerator(NeedleActor.class, ActorId.NEEDLE, powerups, 22, 64, 90, 1, 30f, assetManager, this));

        maxZoom = Math.min(width /stage.getViewport().getWorldWidth(),
                height /stage.getViewport().getWorldHeight());
    }

    /**
     * Actualiza el estado del mundo.
     * @param delta Segundos transcurridos desde la última actualización.
     */
    public void render(float delta) {
        if (!isPaused()) {
            runningTime += delta;

            for (int i = 0; i < actorGenerators.size; i++) {
                actorGenerators.get(i).render(delta);
            }

            stage.act();
        }
    }

    /**
     * Especifica si el juego está en pausa o no.
     * @param paused True si y solo si el juego está pausado.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * @return {@link #paused}
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return {@link #stage}
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return {@link #width}
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return {@link #height}
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return {@link #rootGroup}
     */
    public Group getRootGroup() {
        return rootGroup;
    }

    /**
     * @return {@link #collisionDispatcher}
     */
    public CollisionDispatcher getCollisionDispatcher() {
        return collisionDispatcher;
    }

    /**
     * @return {@link #playerActor}
     */
    public PlayerActor getPlayerActor() {
        return playerActor;
    }

    /**
     * @return {@link #maxZoom}
     */
    public float getMaxZoom() {
        return maxZoom;
    }

    /**
     * @return El número actual de rollos de papel higiénico recolectados.
     */
    public int getPaperCount() {
        return playerActor.getPaperCount();
    }

    /**
     * @return {@link #runningTime}
     */
    public float getRunningTime() {
        return runningTime;
    }

    /**
     * @return True si y solo si la partida ha finalizado.
     */
    public boolean gameIsOver() {
        return !playerActor.isAlive();
    }

    /**
     * Altera el zoom actual por una diferencia.
     * @param delta Diferencia del nuevo zoom con respecto al valor actual.
     */
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

    /**
     * Libera los recursos consumidos por los actores, generadores, etc.
     */
    public void dispose() {
        for (ActorGenerator actorGenerator : actorGenerators) {
            actorGenerator.dispose();
        }

        stage.dispose();
        collisionDispatcher.dispose();
    }

    /**
     * Stage del mundo. Tiene como característica que gestiona automáticamente la
     * posición de la cámara al dibujarse para implementar el scroll con el que se
     * sigue a {@link #playerActor}.
     */
    private class WorldStage extends Stage {
        /**
         * Cámara del mundo.
         */
        private final OrthographicCamera camera;

        /**
         * Inicializa el Stage.
         * @param viewport Viewport del Stage.
         */
        public WorldStage(Viewport viewport) {
            super(viewport);
            camera = (OrthographicCamera)getCamera();
        }

        @Override
        public void draw() {
            adjustCamera();
            super.draw();
        }

        /**
         * Ajusta la posición de la cámara en función de la posición de {@link #playerActor}
         * en el mundo.
         */
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
