# La Pandemia

- [Introducción](#introducción)
- [Historia, objetivo y normas](#historia-objetivo-y-normas)
- [Instalación](#instalación)
  - [Clona localmente el repositorio y accede a él](#clona-localmente-el-repositorio-y-accede-a-él)
  - [Establece la variable de entorno ANDROID_SDK_ROOT](#establece-la-variable-de-entorno-android_sdk_root)
  - [Ejecuta la tarea assembleDebug](#ejecuta-la-tarea-assembledebug)

## Introducción
_La Pandemia_ es un juego para dispositivos Android que he desarrollado como proyecto del segundo trimestre de _Programación Multimedia y Dispositivos Móviles_, uno de los módulos del segundo curso del _Grado Superior en Desarrollo de Aplicaciones Multiplataforma_ que he cursado en el Colegio Vivas.

## Historia, objetivo y normas
![Cuenta atrás](markdown/countdown.gif)
![Cambiando el zoom](markdown/zooming-out.gif)
![Moviéndose](markdown/moving-around-1.gif)
![Recolectando powerups](markdown/getting-powerups.gif)
![Vacuna](markdown/vaccine.gif)
![Salud](markdown/health.gif)
![Pausando y reanudando](markdown/pausing.gif)
![Muriendo](markdown/dying.gif)

En _La Pandemia_, un personaje principal se encuentra encerrado en un mapa de juego a elección del usuario en el que aparecen cada vez más _virus_, que se mueven de manera aleatoria. El jugador posee un número determinado de mascarillas que va perdiendo según entra en contacto con los virus, hasta que finalmente colisiona con uno sin tener ninguna mascarilla, terminándose así la partida.

El jugador obtiene mascarillas recolectándolas por el mapa de juego, donde van apareciendo también aleatoriamente de forma periódica. Además de estas, también aparecen rollos de papel higiénico. El objetivo del juego es coleccionar todos los rollos que se pueda hasta que la partida se termine, y la puntuación final es el recuento total de rollos obtenidos.

Existe una presentación en vídeo mucho más completa en la que se explica con más detenimiento todo lo que tiene para ofrecer este juego. Pronto será subida al canal de YouTube del colegio, y una vez se haya hecho la adjuntaré aquí.

## Instalación
Actualmente, el juego no está disponible en tiendas de aplicaciones como Google Play. Por tanto, para jugar se debe compilar el APK, descargarlo en el dispositivo y ejecutarlo desde él.

Para compilar el APK, se debe tener instalado el SDK de Android con soporte para el nivel de API 14 (para Android 4.0) o superior y ejecutar la tarea de Gradle ```assembleDebug```.

A continuación y a modo de ejemplo se indica cómo se puede lograr esto en un entorno UNIX:

### Clona localmente el repositorio y accede a él
Naturalmente, se requiere tener Git instalado en el sistema.
```
git clone https://github.com/0jc/LaPandemia
cd LaPandemia
```

### Establece la variable de entorno ANDROID_SDK_ROOT
En caso de no estarlo ya, se debe establecer la variable de entorno ```ANDROID_SDK_ROOT``` al directorio raíz del SDK de Android. La ruta del siguiente ejemplo es habitual pero, dependiendo de cómo se haya instalado el SDK, puede no ser la correcta.
```
export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
```

### Ejecuta la tarea assembleDebug
Desde el directorio del repositorio se ejecuta:
```
./gradlew assembleDebug
```
Si la tarea es exitosa, se habrá creado el APK en ```./android/build/outputs/apk/debug/android-debug.apk```.
