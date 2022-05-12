package com.cherashev.candles.repository

import com.cherashev.candles.entity.StockKey
import com.cherashev.candles.entity.StockTicksList
import com.cherashev.candles.entity.Tick
import reactor.core.publisher.Flux

interface TicksRepository {
    fun getAllStockKeys(stock: String): Flux<String>
    fun getAllStockKeys(stock: String, from: Long?, to: Long?): Flux<StockKey>
    fun getAllLists(keys: Flux<StockKey>): Flux<StockTicksList>
    fun saveTick(tick: Tick)
}
