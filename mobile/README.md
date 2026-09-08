# AdaptiveQuiz Mobile

MVP Android de AdaptiveQuiz, implementado en Kotlin, Jetpack Compose y Material 3.

## Estructura

- core/: red, tema visual y utilidades.
- data/: DTO, Retrofit y repositorio remoto.
- domain/: modelos y contrato de repositorio.
- feature/: Inicio, Práctica, Resultado, Monitor Adaptativo y Progreso.
- navigation/: navegación Compose.

La UI está en español y no decide dificultad ni reglas. El flujo es:

~~~text
ViewModel → Repository → API real → estado visual
~~~

El cronómetro se inicia al presentar un ejercicio y envía el tiempo real al endpoint de intentos.
La pista sólo se muestra cuando el backend devuelve pistaHabilitada=true.

## Configuración y build

Por defecto, el emulador usa http://10.0.2.2:8080/. La URL se cambia una sola vez mediante
adaptiveQuizApiBaseUrl en gradle.properties, que pasa a BuildConfig.

~~~powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\\Android\\Sdk"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
.\\gradlew.bat lintDebug assembleDebug
~~~

El APK queda en app/build/outputs/apk/debug/app-debug.apk.

La aplicación soporta visualmente ejercicios OPCION_UNICA. El modelo backend conserva los demás
tipos para una iteración posterior.
