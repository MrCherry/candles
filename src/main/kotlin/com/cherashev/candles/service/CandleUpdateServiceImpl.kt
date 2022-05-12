package com.cherashev.candles.service

import com.cherashev.candles.entity.Tick
import com.cherashev.candles.entity.timestampMinute
import com.cherashev.candles.helper.buildInitialCandle
import com.cherashev.candles.mapper.toMap
import com.cherashev.candles.repository.CandlesRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CandleUpdateServiceImpl(
    val candlesRepository: CandlesRepository
) : CandleUpdateService {
    private val logger = KotlinLogging.logger {}

    override fun updateCandleWithTick(tick: Tick) {
        val received = candlesRepository.getCandleMapForTick(tick)

        val update = received.map { candle ->
            logger.trace { "——— GOT CANDLE: $candle" }

            if (candle.isEmpty()) {
                buildInitialCandle(tick).toMap()
            } else {
                val newMap = mutableMapOf(
                    "close" to tick.price.toString(),
                )

                if (tick.price > candle["high"]!!.toDouble())
                    newMap["high"] = tick.price.toString()
                else if (tick.price < candle["low"]!!.toDouble())
                    newMap["low"] = tick.price.toString()

                newMap.toMap()
            }
        }

        update.map {
            candlesRepository.saveCandleHash(tick.stock, tick.timestampMinute(), it)
        }.subscribe()
    }
}
