package com.cherashev.candles.service

import com.cherashev.candles.entity.Candle
import com.cherashev.candles.entity.StockTicksList
import com.cherashev.candles.repository.TicksRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.function.BiFunction

@Service
class CandlesServiceImpl(val ticksRepository: TicksRepository) : CandlesService {
    private val logger = KotlinLogging.logger {}

    private fun buildCandles(lists: Flux<StockTicksList>) =
        lists.map { stockTicksList ->
            logger.debug { "——— LIST: ${stockTicksList.key.stock}}" }

            val candle = buildInitialCandle(
                stock = stockTicksList.key.stock,
                timestamp = stockTicksList.key.timestamp
            )

            stockTicksList.ticks
                .reduce(candle, reduceCandle)
                .mapNotNull { it!! }
                .toFlux()
        }

    override fun getCandlesForStock(stock: String, period: String?, from: Long?, to: Long?): Flux<Candle> {
        val keys = ticksRepository.getAllStockKeys(stock, from, to)
        val lists = ticksRepository.getAllLists(keys)
        val candles = buildCandles(lists).flatMap { it!! }

        return candles
    }

    companion object {
        private fun buildInitialCandle(stock: String, timestamp: Long) =
            Candle(
                time = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC),
                open = 0.0,
                high  = 0.0,
                low   = Double.MAX_VALUE,
                close = 0.0,
                stock = stock
            )

        val reduceCandle = BiFunction<Candle, Double, Candle> { accumulator, next ->
            val open = if (accumulator.open > 0) accumulator.open else next
            val high = accumulator.high.coerceAtLeast(next)
            val low = accumulator.low.coerceAtMost(next)

            Candle(
                time = accumulator.time,
                stock = accumulator.stock,
                open = open,
                high  = high,
                low   = low,
                close = next
            )
        }
    }
}