package com.cherashev.candles.entity

import kotlinx.serialization.Serializable

@Serializable
data class Tick(
    val time: Long,
    val price: Double,
    val stock: String
)
