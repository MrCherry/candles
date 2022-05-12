package com.cherashev.candles.service

import com.cherashev.candles.entity.Tick

interface CandleUpdateService {
    fun updateCandleWithTick(tick: Tick)
}