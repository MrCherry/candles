package com.cherashev.candles.handler

import com.cherashev.candles.entity.StockParameterRequired
import com.cherashev.candles.entity.Candle
import com.cherashev.candles.service.CandlesService
import com.cherashev.candles.client.TicksClient
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CandleHandler(val candlesService: CandlesService, val ticksClient: TicksClient) {
    fun startReceiving(request: ServerRequest?): Mono<ServerResponse> {
        ticksClient.subscribe()

        return ServerResponse
            .ok()
            .contentType(TEXT_PLAIN)
            .bodyValue("Started receiving ticks data")
    }

    fun stopReceiving(request: ServerRequest?): Mono<ServerResponse> {
        ticksClient.unsubscribe()

        return ServerResponse
            .ok()
            .contentType(TEXT_PLAIN)
            .bodyValue("Stopped receiving ticks data")
    }

    fun getAllCandles(request: ServerRequest): Mono<ServerResponse> {
        val stock = request.queryParam("stock")
            .orElseThrow { StockParameterRequired("parameter stock is required") }

        val period: String? = request.queryParam("period").orElse(null)
        val from: Long? = request.queryParam("from").map(String::toLong).orElse(null)
        val to: Long? = request.queryParam("to").map(String::toLong).orElse(null)

        return ServerResponse
            .ok()
            .contentType(APPLICATION_JSON)
            .body(
                candlesService.getCandlesForStock(stock, period, from, to),
                Candle::class.java
            )
    }
}
