package com.cherashev.candles.entity

import kotlin.Comparable

data class StockKey(
    val key: String,
    val stock: String,
    val timestamp: Long
) : Comparable<StockKey> {
    override operator fun compareTo(other: StockKey): Int {
        return timestamp.compareTo(other.timestamp)
    }
}
