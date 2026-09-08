# CI objetivo

Codex deberá crear:
- `backend-ci.yml`: `mvn -B clean verify` con JDK 17.
- `android-ci.yml`: Android SDK Platform 37, `lintDebug` y `assembleDebug`.
- `quality.yml`: opcional.

Los workflows principales bloquean la creación del tag/release v1.0.0 hasta que resulten verdes.
