package com.colegiovivas.lapandemia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.colegiovivas.lapandemia.hardware.HardwareWrapper;
import com.colegiovivas.lapandemia.levels.LevelCatalog;
import com.colegiovivas.lapandemia.levels.LevelInfo;
import com.colegiovivas.lapandemia.screens.credits.CreditsScreen;
import com.colegiovivas.lapandemia.screens.main.MainMenuScreen;
import com.colegiovivas.lapandemia.screens.mapselection.MapSelectionScreen;
import com.colegiovivas.lapandemia.screens.preview.PreviewScreen;
import com.colegiovivas.lapandemia.screens.results.ResultsScreen;
import com.colegiovivas.lapandemia.screens.game.GameScreen;
import com.colegiovivas.lapandemia.screens.LoadingScreen;
import com.colegiovivas.lapandemia.screens.settings.SettingsScreen;

/**
 * La clase principal del juego. Coordina la gestión de las pantallas y ofrece
 * acceso a varios elementos de uso global en la aplicación, como por ejemplo
 * los fondos de diversas clases, que permiten reciclar instancias liberadas
 * o preparadas de objetos de una clase determinada en lugar de tener que
 * instanciar otros nuevos para reducir la actividad del recolector de basura
 * y mejorar así el rendimiento del juego.
 */
public class LaPandemia extends Game {
    /**
     * El gestor de assets (recursos como imágenes y música). Permite cargar una
     * única vez dichos recursos, acceder a ellos fácilmente cuando es necesario
     * y luego finalmente liberarlos.
     */
    private AssetManager assetManager;

    /**
     * Interfaz hacia el giroscopio y vibrador en función de la configuración establecida
     * por el usuario.
     */
    private HardwareWrapper hardwareWrapper;

    /**
     * La pantalla a la que saltar después de cargar los recursos. Si es null, se
     * entiende que la aplicación se acaba de iniciar y se carga el menú principal.
     * Al producirse ciertos eventos como la pausa de la aplicación, se establece
     * el valor de esta propiedad al de la pantalla actual para poder restaurarla
     * después tras la carga de recursos.
     */
    private Screen nextScreen = null;

    /**
     * Interfaz hacia el catálogo de niveles disponibles.
     */
    private LevelCatalog levelCatalog;

    /**
     * Inicializa una instancia de la aplicación.
     * @see Game#create()
     */
    @Override
    public void create() {
        Gdx.app.log("LaPandemia", "create()");

        levelCatalog = new LevelCatalog();
        assetManager = new AssetManager();
        hardwareWrapper = new HardwareWrapper();

        setScreen(new LoadingScreen(this, assetManager));
    }

    @Override
    public void pause() {
        super.pause();
        nextScreen = getScreen();
    }

    @Override
    public void resume() {
        super.resume();
        setScreen(new LoadingScreen(this, assetManager));
    }

    /**
     * Evento que un LoadingScreen lanza cuando ha cargado los recursos del juego.
     * @param loadingScreen La pantalla que ha lanzado el evento.
     */
    public void resourcesLoaded(LoadingScreen loadingScreen) {
        loadingScreen.dispose();

        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        } else {
            setScreen(new MainMenuScreen(this, assetManager));
        }
    }

    /**
     * Evento que un MainMenuScreen lanza cuando se ha elegido la opción de mostrar
     * los mapas disponibles.
     * @param mainMenuScreen La pantalla que ha lanzado el evento.
     */
    public void mapSelectionScreenChosen(MainMenuScreen mainMenuScreen) {
        mainMenuScreen.dispose();

        setScreen(new MapSelectionScreen(this, levelCatalog, assetManager));
    }

    /**
     * Evento que un MainMenuScreen lanza cuando se ha elegido la opción de mostrar
     * los créditos.
     * @param mainMenuScreen La pantalla que ha lanzado el evento.
     */
    public void creditsScreenChosen(MainMenuScreen mainMenuScreen) {
        mainMenuScreen.dispose();

        setScreen(new CreditsScreen(this, assetManager));
    }

    /**
     * Evento que un MainScreenMenu lanza cuando se ha elegido la opción de mostrar
     * los ajustes.
     * @param mainMenuScreen La pantalla que ha lanzado el evento.
     */
    public void settingsScreenChosen(MainMenuScreen mainMenuScreen) {
        mainMenuScreen.dispose();

        setScreen(new SettingsScreen(this, hardwareWrapper, assetManager));
    }

    /**
     * Evento que un PreviewScreen lanza cuando se ha elegido la opción de volver
     * a la pantalla de selección de mapas.
     * @param previewScreen La pantalla que ha lanzado el evento.
     */
    public void navigatedBackToMapSelection(PreviewScreen previewScreen) {
        previewScreen.dispose();

        setScreen(new MapSelectionScreen(this, levelCatalog, assetManager));
    }

    /**
     * Evento que un MapSelectionScreen lanza cuando se ha elegido un mapa.
     * @param source La pantalla que ha lanzado el evento.
     * @param levelInfo El nivel que se ha elegido.
     */
    public void previewScreenChosen(MapSelectionScreen source, LevelInfo levelInfo) {
        source.dispose();

        setScreen(new PreviewScreen(this, levelInfo, assetManager));
    }

    /**
     * Evento que una pantalla lanza cuando se ha elegido volver a la pantalla
     * principal.
     * @param screen La pantalla que ha lanzado el evento.
     */
    public void navigatedBackToMain(Screen screen) {
        screen.dispose();

        setScreen(new MainMenuScreen(this, assetManager));
    }

    /**
     * Evento que un PreviewScreen lanza cuando se ha elegido la opción de jugar
     * en el nivel mostrado.
     * @param source La pantalla que ha lanzado el evento.
     * @param levelInfo Nivel en el que se ha elegido jugar.
     */
    public void mapChosen(PreviewScreen source, LevelInfo levelInfo) {
        source.dispose();

        setScreen(new GameScreen(this, levelInfo, hardwareWrapper, assetManager));
    }

    /**
     * Evento que un GameScreen lanza cuando ha finalizado su actividad.
     * @param gameScreen La pantalla que ha lanzado el evento.
     * @param level Nivel en el que se ha jugado la partida.
     * @param paperCount Número de rollos de papel recolectados.
     * @param runningTime Tiempo de juego desde el final de la cuenta atrás hasta haber perdido.
     */
    public void gameOver(GameScreen gameScreen, LevelInfo level, int paperCount, float runningTime) {
        gameScreen.dispose();

        setScreen(new ResultsScreen(this, level, assetManager, paperCount, runningTime));
    }

    /**
     * Evento que un ResultsScreen lanza cuando ha finalizado su actividad.
     * @param resultsScreen La pantalla que ha lanzado el evento.
     */
    public void resultsAccepted(ResultsScreen resultsScreen, boolean playAgain, LevelInfo sourceLevel) {
        resultsScreen.dispose();

        if (playAgain) {
            setScreen(new GameScreen(this, sourceLevel, hardwareWrapper, assetManager));
        } else {
            setScreen(new MainMenuScreen(this, assetManager, true));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) getScreen().dispose();
        assetManager.dispose();
    }

    /**
     * @return {@link #assetManager}
     */
    public AssetManager getAssetManagerOld() {
        return assetManager;
    }

    /**
     * @param assetManager El nuevo valor para {@link #assetManager}.
     */
    public void setAssetManagerOld(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * @return {@link #hardwareWrapper}
     */
    public HardwareWrapper getHardwareWrapperOld() {
        return hardwareWrapper;
    }

    /**
     * @param hardwareWrapper El nuevo valor para {@link #hardwareWrapper}.
     */
    public void setHardwareWrapperOld(HardwareWrapper hardwareWrapper) {
        this.hardwareWrapper = hardwareWrapper;
    }
}
