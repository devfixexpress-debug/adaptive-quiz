# Validación de build y runtime Android

Fecha de ejecución: 2026-09-08.

## Entorno

- JDK: Eclipse Temurin 17.0.17.
- Gradle: 9.3.1.
- Android Gradle Plugin: 9.1.1.
- Kotlin Compose: 2.2.10.
- Android SDK Platform: 37.
- Paquete: com.veltia.adaptivequiz.mobile.
- Dispositivos ADB disponibles: teléfono CPH3669 y emulador emulator-5554.

## Build

Comando ejecutado desde mobile:

~~~powershell
.\gradlew.bat lintDebug assembleDebug --no-daemon
~~~

Se generó el APK debug en:

~~~text
mobile/app/build/outputs/apk/debug/app-debug.apk
~~~

Lint completó con 0 errores y 11 advertencias no bloqueantes de actualización de herramientas y
librerías, elegidas deliberadamente para la combinación compatible instalada; se corrigieron las
advertencias propias de icono, recurso de nombre y target SDK mediante icono vectorial,
targetSdk 37 y referencia a app_name.

## Smoke real en emulador

1. Se instaló el APK con ADB en emulator-5554.
2. Se inició MainActivity y no hubo FATAL EXCEPTION.
3. Inicio mostró AdaptiveQuiz, Estudiante Demo, Matemática y Álgebra recibidos por Retrofit desde
   el backend real en 10.0.2.2:8080.
4. COMENZAR PRÁCTICA creó la sesión 3 y mostró ejercicio ALG-B-001 en dificultad BASICO.
5. La UI mostró MOSTRAR PISTA porque el backend retornó pistaHabilitada=true.
6. Se eligió la opción C/4 y RESPONDER; la pantalla Resultado mostró Correcto, explicación,
   R_MEDIO y BASICO → BASICO.
7. VER MONITOR ADAPTATIVO mostró contexto, procesamiento, decisión y adaptación.
8. En ese smoke, Progreso mostró 7 intentos, 57 %, BASICO, racha de 3 aciertos e historial real.
   La sesión interactiva posterior dejó 14 intentos/eventos persistidos.

## Incidencia corregida durante la prueba

El primer arranque mostró socket failed: EPERM al cargar el catálogo. El manifiesto de la
aplicación no declaraba android.permission.INTERNET. Se añadió el permiso, se reconstruyó el APK
y la segunda instalación cargó los datos reales correctamente.

## Alcance visual

La revisión se efectuó en emulador Android real. El teléfono físico también fue reconocido por
ADB, pero no se instaló una variante para él porque la configuración debug predeterminada
10.0.2.2 está destinada al emulador. Para teléfono físico se cambia una sola propiedad:
adaptiveQuizApiBaseUrl.
