package com.cherashev.candles.entity

import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.time.ZoneOffset

@Serializable
data class Tick(
    val time: Long,
    val price: Double,
    val stock: String
)

fun Tick.timestampMinute() = Timestamp(time)
    .toLocalDateTime()
    .withSecond(0).withNano(0)
    .toEpochSecond(ZoneOffset.UTC)
