package com.colegiovivas.lapandemia;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Lanzador de la aplicación.
 */
public class AndroidLauncher extends AndroidApplication {
    /**
     * Evento que se lanza al crearse una instancia de la aplicación. Permite configurar
     * aspectos tales como qué sensores hardware se utilizarán y crea una instancia de
     * la clase principal, {@link LaPandemia}.
     * @param savedInstanceState Bundle de Android con información de la actividad.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useAccelerometer = false;
        config.useCompass = false;
        config.useGyroscope = true;

        initialize(new LaPandemia(), config);
    }
}
