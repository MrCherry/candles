package com.cherashev.candles.dto

import kotlinx.serialization.Serializable

@Serializable
data class TicksApiResponseDto(
    val data: List<TicksApiResponseDataDto>,
    val type: String
)

