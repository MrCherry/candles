package com.cherashev.candles.entity

import kotlinx.serialization.Serializable

@Serializable
data class Candle(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val stock: String
)

fun Candle.getKey() = "candle_${stock}_${time}"
