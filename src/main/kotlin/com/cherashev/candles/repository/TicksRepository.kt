package com.cherashev.candles.repository

import com.cherashev.candles.entity.StockTicksKey
import com.cherashev.candles.entity.StockTicksList
import reactor.core.publisher.Flux

interface TicksRepository {
    fun getAllStockKeys(stock: String): Flux<String>
    fun getAllStockKeys(stock: String, from: Long?, to: Long?): Flux<StockTicksKey>
    fun getAllLists(keys: Flux<StockTicksKey>): Flux<StockTicksList>
}
