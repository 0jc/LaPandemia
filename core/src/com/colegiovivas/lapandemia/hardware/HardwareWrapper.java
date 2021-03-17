package com.colegiovivas.lapandemia.hardware;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Interfaz hacia los componentes hardware utilizados en el juego. Permite configurar
 * su comportamiento para que las demás clases puedan utilizarlos de forma transparente
 * a sus configuraciones.
 */
public class HardwareWrapper {
    /**
     * Preferencias compartidas del giroscopio.
     */
    private final Preferences gyroscopePrefs;

    /**
     * Preferencias compartidas del vibrador.
     */
    private final Preferences vibratorPrefs;

    /**
     * True si y solo si el giroscopio está activado. Sirve de caché para no tener que acceder
     * constantemente a las preferencias compartidas.
     */
    boolean gyroscopeEnabled;

    /**
     * True si y solo si el vibrador está activado. Sirve de caché para no tener que acceder
     * constantemente a las preferencias compartidas.
     */
    boolean vibratorEnabled;

    /**
     * Inicializa el contenedor.
     */
    public HardwareWrapper() {
        gyroscopePrefs = Gdx.app.getPreferences("gyroscope");
        vibratorPrefs = Gdx.app.getPreferences("vibrator");

        gyroscopeEnabled = gyroscopePrefs.getBoolean("enabled", true);
        vibratorEnabled = vibratorPrefs.getBoolean("enabled", true);
    }

    /**
     * @param enabled Si el giroscopio estará o no activado.
     */
    public void setGyroscopeEnabled(boolean enabled) {
        gyroscopePrefs.putBoolean("enabled", enabled);
        gyroscopePrefs.flush();
        gyroscopeEnabled = enabled;
    }

    /**
     * @return Si el giroscopio está o no activado.
     */
    public boolean getGyroscopeEnabled() {
        return gyroscopeEnabled;
    }

    /**
     * @param enabled Si el vibrador está o no activado.
     */
    public void setVibratorEnabled(boolean enabled) {
        vibratorPrefs.putBoolean("enabled", enabled);
        vibratorPrefs.flush();
        vibratorEnabled = enabled;
    }

    /**
     * @return Si el vibrador está o no activado.
     */
    public boolean getVibratorEnabled() {
        return vibratorEnabled;
    }

    /**
     * Lanza el vibrador, en caso de que este esté activado. Si no lo está, no se realiza
     * ninguna acción.
     * @param milliseconds Milisegundos de vibración en caso de que el vibrador esté activado.
     */
    public void vibrate(int milliseconds) {
        if (vibratorEnabled) {
            Gdx.input.vibrate(milliseconds);
        }
    }

    /**
     * @return El valor de Gdx.input.getGyroscopeY(), en caso de que el giroscopio esté activado,
     * o 0 si no lo está.
     */
    public float getGyroscopeY() {
        return gyroscopeEnabled ? Gdx.input.getGyroscopeY() : 0;
    }
}
