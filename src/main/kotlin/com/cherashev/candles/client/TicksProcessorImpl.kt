package com.cherashev.candles.client

import com.cherashev.candles.dto.TicksApiResponseDto
import com.cherashev.candles.mapper.toTick
import com.cherashev.candles.service.CandleUpdateService
import org.springframework.stereotype.Component
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.context.annotation.Primary

@Primary
@Component
class TicksProcessorImpl(
    val candleUpdateService: CandleUpdateService
) : TicksProcessor {
    private val logger = KotlinLogging.logger {}

    override fun processRawTick(rawTick: String) {
        candleUpdateService.updateCandleWithTick(
            parseRawTickToDto(rawTick).toTick()
        )
    }

    private fun parseRawTickToDto(rawTick: String) =
        Json.decodeFromString(
            TicksApiResponseDto.serializer(),
            rawTick
        )
}
