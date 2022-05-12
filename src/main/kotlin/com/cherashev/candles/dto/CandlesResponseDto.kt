package com.cherashev.candles.dto

import java.time.LocalDateTime

data class CandlesResponseDto(
    val time: LocalDateTime,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val stock: String
)
