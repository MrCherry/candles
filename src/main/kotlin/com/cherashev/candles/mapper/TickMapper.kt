package com.cherashev.candles.mapper

import com.cherashev.candles.dto.TicksApiResponseDto
import com.cherashev.candles.entity.Tick

fun TicksApiResponseDto.toTick() = Tick(
    time = data.first().t,
    price = data.first().p,
    stock = data.first().s
)
