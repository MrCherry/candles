package com.cherashev.candles.dto

import kotlinx.serialization.Serializable

@Serializable
data class TicksApiResponseDataDto(
    val p: Double,
    val s: String,
    val t: Long,
    val v: Int
)
