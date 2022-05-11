package com.cherashev.candles.entity

import kotlin.Comparable

data class StockTicksKey(
    val key: String,
    val stock: String,
    val timestamp: Long
) : Comparable<StockTicksKey> {
    override operator fun compareTo(other: StockTicksKey): Int {
        return timestamp.compareTo(other.timestamp)
    }
}
