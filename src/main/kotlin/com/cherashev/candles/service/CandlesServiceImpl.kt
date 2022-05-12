package com.cherashev.candles.service

import com.cherashev.candles.entity.Candle
import com.cherashev.candles.repository.CandlesRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
@Primary
@Service
class CandlesServiceImpl(
    val candlesRepository: CandlesRepository
) : CandlesService {
    override fun getCandlesForStock(stock: String, period: String?, from: Long?, to: Long?): Flux<Candle> {
        return candlesRepository.getAllCandlesForStock(stock, from, to)
    }
}
