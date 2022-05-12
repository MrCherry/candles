package com.cherashev.candles.repository

import com.cherashev.candles.entity.Candle
import com.cherashev.candles.entity.Tick
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CandlesRepository {
    fun getAllCandlesForStock(stock: String, from: Long?, to: Long?): Flux<Candle>
    fun saveCandle(candle: Candle)
    fun getCandleMapForTick(tick: Tick): Mono<Map<String, String>>
    fun getCandleForTick(tick: Tick): Mono<Candle>
    fun saveCandleHash(candle: Candle)
    fun saveCandleHash(stock: String, time: Long, fields: Map<String, String>)
    fun getCandle(candleKey: String): Mono<Candle>
}