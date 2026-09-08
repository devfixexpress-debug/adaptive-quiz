package com.veltia.adaptivequiz.mobile.core.util

import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion

data class AdaptacionVisual(
    val titulo: String,
    val mensaje: String,
    val esRefuerzo: Boolean
)

fun Adaptacion.toVisual(): AdaptacionVisual = when {
    accionPrincipal == "SUBIR_DIFICULTAD" -> AdaptacionVisual(
        "Tu desempeño está mejorando",
        "Pasamos de ${dificultadAnterior.titulo()} a ${dificultadNueva.titulo()}.",
        false
    )
    pistaHabilitada -> AdaptacionVisual(
        "Vamos a reforzar el tema",
        "Activamos una pista y ajustamos la dificultad para la siguiente experiencia.",
        true
    )
    else -> AdaptacionVisual(
        "Seguimos consolidando el aprendizaje",
        "Mantenemos ${dificultadNueva.titulo()} con la regla ${regla ?: "configurada"}.",
        false
    )
}

fun String.titulo(): String = lowercase().replaceFirstChar { it.titlecase() }
