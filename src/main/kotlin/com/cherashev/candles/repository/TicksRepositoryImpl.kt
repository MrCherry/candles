package com.cherashev.candles.repository

import com.cherashev.candles.entity.StockTicksKey
import com.cherashev.candles.entity.StockTicksList
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class TicksRepositoryImpl(val ticksOps: ReactiveRedisOperations<String, Double>) : TicksRepository {
    private val logger = KotlinLogging.logger {}

    override fun getAllStockKeys(stock: String) = ticksOps.keys("${stock}_*")

    override fun getAllStockKeys(stock: String, from: Long?, to: Long?) : Flux<StockTicksKey> {
        logger.debug { "——— KEYS FILTERING: $from, $to" }

        val ticksKeys = getAllStockKeys(stock)
            .map { ticksKey ->
                val (stockName, timestamp) = ticksKey.split("_")
                StockTicksKey(ticksKey, stockName, timestamp.toLong())
            }

        from?.let {
            ticksKeys.filter { ticksKey ->
                ticksKey.timestamp >= from
            }
        }

        to?.let {
            ticksKeys.filter { ticksKey ->
                ticksKey.timestamp <= to
            }
        }

        return ticksKeys.sort()
    }

    override fun getAllLists(keys: Flux<StockTicksKey>) =
        keys.map { stockKey ->
            logger.debug { "——— KEY: Stock - ${stockKey.stock}, Timestamp - ${stockKey.timestamp}" }

            StockTicksList(
                key = stockKey,
                ticks = ticksOps.opsForList().range(stockKey.key, 0, -1)
            )
        }
}
