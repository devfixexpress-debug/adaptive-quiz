# CI objetivo

Codex deberá crear:
- `backend-ci.yml`: compile + unit/integration tests.
- `android-ci.yml`: lint + unit tests + assembleDebug.
- `quality.yml`: opcional.

No bloquear M0 por CI; sí bloquear release v1.0.0 si los pipelines principales fallan.
