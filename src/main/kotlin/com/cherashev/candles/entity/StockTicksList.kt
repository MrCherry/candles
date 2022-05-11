package com.cherashev.candles.entity

import reactor.core.publisher.Flux

data class StockTicksList(
    val key: StockTicksKey,
    val ticks: Flux<Double>
)
