package com.veltia.adaptivequiz.mobile.core.util

import kotlin.math.max

object TiempoRespuesta {
    fun transcurridoMs(inicioMs: Long, ahoraMs: Long): Int =
        max(0L, ahoraMs - inicioMs).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()

    fun segundos(ms: Int): String = "%.1f s".format(ms / 1000.0)
}
