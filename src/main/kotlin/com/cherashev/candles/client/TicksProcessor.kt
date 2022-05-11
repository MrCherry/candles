package com.cherashev.candles.client

import com.cherashev.candles.dto.TicksApiResponseDto
import com.cherashev.candles.entity.Tick
import com.cherashev.candles.mapper.toTick
import org.springframework.stereotype.Component
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisOperations
import java.sql.Timestamp
import java.time.Duration
import java.time.ZoneOffset

@Component
class TicksProcessor(
    val ticksOps: ReactiveRedisOperations<String, Double>
) {
    private val logger = KotlinLogging.logger {}

    fun processRawTick(rawTick: String): Boolean {
        val tickDto = parseTick(rawTick)
        return saveTick(tickDto)
    }

    fun parseTick(rawTick: String): TicksApiResponseDto {
        return Json.decodeFromString(
            TicksApiResponseDto.serializer(),
            rawTick
        )
    }

    fun saveTick(tickDto: TicksApiResponseDto): Boolean {
        val tick: Tick = tickDto.toTick()

        val timestampMinute = Timestamp(tick.time)
            .toLocalDateTime()
            .withSecond(0).withNano(0)
            .toEpochSecond(ZoneOffset.UTC)

        val listKey = "${tick.stock}_${timestampMinute}"


        ticksOps.opsForList().rightPush(listKey, tick.price).and(
            ticksOps.expire(listKey, Duration.ofDays(1))
        ).subscribe()

        return true
    }
}
