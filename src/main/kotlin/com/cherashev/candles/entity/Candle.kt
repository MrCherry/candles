package com.cherashev.candles.entity

import java.time.LocalDateTime

data class Candle(
    val time: LocalDateTime,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val stock: String
)
