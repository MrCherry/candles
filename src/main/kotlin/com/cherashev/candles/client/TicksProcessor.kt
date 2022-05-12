package com.cherashev.candles.client

interface TicksProcessor {
    fun processRawTick(rawTick: String)
}
