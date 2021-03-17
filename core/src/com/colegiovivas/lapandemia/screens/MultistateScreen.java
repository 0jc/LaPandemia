package com.colegiovivas.lapandemia.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.Iterator;

/**
 * Pantalla orquestada mediante "estados". Permiten programar por separado cómo
 * se debe comportar la pantalla en diferentes fases del desarrollo del juego.
 * Por ejemplo, en una pantalla dedicada a mostrar una partida, la fase de la
 * cuenta atrás se puede considerar un estado de la pantalla distinto a la fase
 * en la que el personaje ya puede moverse o a cuando la partida está pausada.
 */
public abstract class MultistateScreen<T extends Enum<T>> implements Screen {
    /**
     * Estados disponibles para la pantalla en función de sus IDs.
     */
    private final ArrayMap<T, State> states;

    /**
     * ID del estado actualmente activo.
     */
    private T currentStateId;

    /**
     * Inicializa los recursos necesarios para la gestión de estados.
     */
    public MultistateScreen() {
        states = new ArrayMap<>();
    }

    @Override
    public void render(float delta) {
        getCurrentState().render(delta);
    }

    @Override
    public void resize(int width, int height) {
        for (State state : states.values()) {
            state.resize(width, height);
        }
    }

    @Override
    public void pause() {
        getCurrentState().pause();
    }

    @Override
    public void resume() {
        getCurrentState().resume();
    }

    @Override
    public void hide() {
        if (currentStateId != null) getCurrentState().hide();
    }

    @Override
    public void show() {
        getCurrentState().show();
    }

    /**
     * Cambia de estado, lanzando los eventos relevantes tanto al estado
     * original, si existía uno, como al nuevo.
     *
     * El estado original recibe el evento {@link State#leave()},
     * mientras que el nuevo recibe los eventos {@link State#enter()} y
     * {@link MultistateScreen#show()}.
     * @param stateId ID del estado al que se quiere cambiar.
     */
    public void setState(T stateId) {
        if (!states.containsKey(stateId)) {
            throw new IllegalArgumentException();
        }

        State oldState = getCurrentState();
        if (oldState != null) {
            oldState.leave();
        }

        currentStateId = stateId;
        states.get(currentStateId).enter();
        states.get(currentStateId).show();
    }

    /**
     * Registra un estado para poder establecerlo más adelante mediante setState.
     * @param id ID mediante la que se identificará al estado.
     * @param state Estado que será añadido.
     */
    public void addState(T id, State state) {
        if (states.containsKey(id) || state == null) {
            throw new IllegalArgumentException();
        }

        states.put(id, state);
    }

    /**
     * Libera los recursos utilizados por la pantalla, incluyendo llamadas a
     * los {@link State#dispose()} de cada estado. También notifica al estado
     * actual de un evento {@link State#leave()}.
     */
    @Override
    public void dispose() {
        if (currentStateId != null) {
            getCurrentState().leave();
        }

        Iterator<State> iterator = states.values();
        while (iterator.hasNext()) {
            State state = iterator.next();
            state.dispose();
        }

        states.clear();
        currentStateId = null;
    }

    /**
     * @return El estado actual o null si no hay ninguno.
     */
    public State getCurrentState() {
        if (currentStateId != null) {
            return states.get(currentStateId);
        } else {
            return null;
        }
    }

    /**
     * Estado para una pantalla de estados múltiples (MultistateScreen).
     */
    public interface State extends Screen {
        /**
         * Evento que se lanza cada vez que se establece el estado como estado actual.
         */
        void enter();

        /**
         * Evento que se lanza cuando el estado deja de ser el actual, ya sea porque
         * se ha cambiado el estado con setState() o porque se está liberando la
         * pantalla con dispose().
         */
        void leave();
    }

    /**
     * Adaptador de State, para evitar tener que implementar métodos vacíos.
     */
    public static abstract class StateAdapter implements State {
        /**
         * Inicializa el estado.
         */
        public StateAdapter() {

        }

        @Override
        public void enter() {

        }

        @Override
        public void leave() {

        }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta) {

        }

        @Override
        public void resize(int width, int height) {

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

        }
    }
}
