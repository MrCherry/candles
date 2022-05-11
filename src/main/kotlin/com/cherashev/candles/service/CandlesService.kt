package com.cherashev.candles.service

import com.cherashev.candles.entity.Candle
import reactor.core.publisher.Flux

interface CandlesService {
    fun getCandlesForStock(stock: String, period: String?, from: Long?, to: Long?): Flux<Candle>
}
