package com.cherashev.candles.repository

import com.cherashev.candles.entity.*
import com.cherashev.candles.helper.candleFromMap
import com.cherashev.candles.mapper.toMap
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.time.Duration

@Repository
class CandlesRepositoryImpl(
    private val candlesOps: ReactiveRedisOperations<String, Candle>,
) : CandlesRepository {
    private val logger = KotlinLogging.logger {}

    fun getAllCandlesKeys(stock: String) = candlesOps.keys("candle_${stock}_*")

    fun getAllCandlesKeys(stock: String, from: Long?, to: Long?) : Flux<StockKey> {
        logger.debug { "——— KEYS FILTERING: $from, $to" }

        val candleKeys = getAllCandlesKeys(stock)
            .map { candleKey ->
                val (_, stockName, timestamp) = candleKey.split("_")
                StockKey(candleKey, stockName, timestamp.toLong())
            }

        from?.let {
            candleKeys.filter { ticksKey ->
                ticksKey.timestamp >= from
            }
        }

        to?.let {
            candleKeys.filter { ticksKey ->
                ticksKey.timestamp <= to
            }
        }

        return candleKeys.sort()
    }

    override fun getAllCandlesForStock(stock: String, from: Long?, to: Long?): Flux<Candle> {
        val keys = getAllCandlesKeys(stock, from, to)

        val candles = keys.map { stockKey ->
            logger.debug { "——— KEY: Stock - ${stockKey.stock}, Timestamp - ${stockKey.timestamp}" }
            getCandle(stockKey.key).toFlux()
        }

        return candles.flatMap { it }
    }

    override fun saveCandle(candle: Candle) {
        val candleKey = candle.getKey()

        candlesOps.opsForValue().set(candleKey, candle).and(
            candlesOps.expire(candleKey, Duration.ofDays(1))
        ).subscribe()
    }

    override fun saveCandleHash(candle: Candle) {
        val candleKey = candle.getKey()

        candlesOps.opsForHash<String, String>().putAll(candleKey, candle.toMap()).and(
            candlesOps.expire(candleKey, Duration.ofDays(1))
        ).subscribe()
    }

    override fun saveCandleHash(stock: String, time: Long, fields: Map<String, String>) {
        val candleKey = "candle_${stock}_${time}"

        candlesOps.opsForHash<String, String>().putAll(candleKey, fields).and(
            candlesOps.expire(candleKey, Duration.ofDays(1))
        ).subscribe()
    }

    override fun getCandle(candleKey: String): Mono<Candle> {
        val entries = candlesOps.opsForHash<String, String>().entries(candleKey)
        return entries.collectMap(Map.Entry<String, String>::key, Map.Entry<String, String>::value).mapNotNull { candleFromMap(it) }
    }

    override fun getCandleMapForTick(tick: Tick): Mono<Map<String, String>> {
        val candleKey = "candle_${tick.stock}_${tick.timestampMinute()}"
        val entries = candlesOps.opsForHash<String, String>().entries(candleKey)
        return entries.collectMap(Map.Entry<String, String>::key, Map.Entry<String, String>::value)
    }

    override fun getCandleForTick(tick: Tick) =
        getCandleMapForTick(tick).mapNotNull { candleFromMap(it) }

}
