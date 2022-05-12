package com.cherashev.candles.repository

import com.cherashev.candles.entity.StockKey
import com.cherashev.candles.entity.StockTicksList
import com.cherashev.candles.entity.Tick
import com.cherashev.candles.entity.timestampMinute
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration

@Repository
class TicksRepositoryImpl(
    private val ticksOps: ReactiveRedisOperations<String, Double>
) : TicksRepository {
    private val logger = KotlinLogging.logger {}

    override fun getAllStockKeys(stock: String) = ticksOps.keys("ticks_${stock}_*")

    override fun getAllStockKeys(stock: String, from: Long?, to: Long?) : Flux<StockKey> {
        logger.debug { "——— KEYS FILTERING: $from, $to" }

        val ticksKeys = getAllStockKeys(stock)
            .map { ticksKey ->
                val (_, stockName, timestamp) = ticksKey.split("_")
                StockKey(ticksKey, stockName, timestamp.toLong())
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

    override fun getAllLists(keys: Flux<StockKey>) =
        keys.map { stockKey ->
            logger.debug { "——— KEY: Stock - ${stockKey.stock}, Timestamp - ${stockKey.timestamp}" }

            StockTicksList(
                key = stockKey,
                ticks = ticksOps.opsForList().range(stockKey.key, 0, -1)
            )
        }

    override fun saveTick(tick: Tick) {
        val listKey = "ticks_${tick.stock}_${tick.timestampMinute()}"

        ticksOps.opsForList().rightPush(listKey, tick.price).and(
            ticksOps.expire(listKey, Duration.ofDays(1))
        ).subscribe()
    }
}
