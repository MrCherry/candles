package com.cherashev.candles.mapper

import com.cherashev.candles.dto.CandlesResponseDto
import com.cherashev.candles.entity.Candle
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Candle.toDto() = CandlesResponseDto(
    time = LocalDateTime.ofEpochSecond(this.time, 0, ZoneOffset.UTC),
    open = this.open,
    high = this.high,
    low = this.low,
    close = this.close,
    stock = this.stock,
)

fun Candle.toMap() = mapOf(
    "time" to this.time.toString(),
    "open" to this.open.toString(),
    "high" to this.high.toString(),
    "low" to this.low.toString(),
    "close" to this.close.toString(),
    "stock" to this.stock
)
