package com.cherashev.candles.client

import com.cherashev.candles.dto.TicksApiResponseDto
import com.cherashev.candles.mapper.toTick
import com.cherashev.candles.repository.TicksRepository
import org.springframework.stereotype.Component
import kotlinx.serialization.json.Json
import mu.KotlinLogging

@Component
class TicksProcessorRealtime(
    val ticksRepository: TicksRepository
) : TicksProcessor {
    private val logger = KotlinLogging.logger {}

    override fun processRawTick(rawTick: String) {
        ticksRepository.saveTick(
            parseRawTickToDto(rawTick).toTick()
        )
    }

    private fun parseRawTickToDto(rawTick: String) =
        Json.decodeFromString(
            TicksApiResponseDto.serializer(),
            rawTick
        )
}
