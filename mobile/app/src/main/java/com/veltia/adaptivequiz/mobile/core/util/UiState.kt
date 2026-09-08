package com.veltia.adaptivequiz.mobile.core.util

sealed interface Carga<out T> {
    data object Cargando : Carga<Nothing>
    data object Vacio : Carga<Nothing>
    data class Exito<T>(val datos: T) : Carga<T>
    data class Error(val mensaje: String) : Carga<Nothing>
}

fun Throwable.mensajeAmigable(): String = message?.takeIf { it.isNotBlank() }
    ?: "No pudimos conectar con AdaptiveQuiz. Verifica la red e inténtalo nuevamente."
