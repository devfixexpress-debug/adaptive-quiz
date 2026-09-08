# Arquitectura IA futura

```text
AdaptationEngine
      |
      v
AdaptationStrategy
   /      |       Rules     AI     Hybrid
```

## Puerto
```java
interface AdaptationStrategy {
    AdaptationDecision evaluate(LearningContext context);
}
```

## Gobierno
- registrar proveedor/modelo;
- registrar versión del prompt;
- no enviar PII innecesaria;
- timeouts y fallback;
- auditoría de origen de decisión;
- `ORIGEN_DECISION = REGLAS | IA | HIBRIDO`.
