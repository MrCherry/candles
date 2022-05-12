package com.cherashev.candles.helper

import com.cherashev.candles.entity.Candle
import com.cherashev.candles.entity.Tick
import com.cherashev.candles.entity.timestampMinute

fun buildInitialCandle(tick: Tick) =
    Candle(
        time = tick.timestampMinute(),
        open = tick.price,
        high  = tick.price,
        low   = tick.price,
        close = tick.price,
        stock = tick.stock
    )

fun buildInitialCandle(stock: String, timestamp: Long) =
    Candle(
        time = timestamp,
        open = 0.0,
        high  = 0.0,
        low   = Double.MAX_VALUE,
        close = 0.0,
        stock = stock
    )

fun candleFromMap(map: Map<String, String>) = Candle(
    time  = map["time"]!!.toLong(),
    open  = map["open"]!!.toDouble() ,
    high  = map["high"]!!.toDouble() ,
    low   = map["low"]!!.toDouble() ,
    close = map["close"]!!.toDouble() ,
    stock = map["stock"]!! ,
)