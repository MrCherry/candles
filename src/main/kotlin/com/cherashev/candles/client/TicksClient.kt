package com.cherashev.candles.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.SignalType
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler
import reactor.core.publisher.Sinks.EmitResult
import java.net.URI
import mu.KotlinLogging

@Component
class TicksClient(val ticksSubscriber: TicksSubscriber) {
    private val logger = KotlinLogging.logger {}
    private val uri: URI = URI.create("ws://localhost:9989")
    private lateinit var sessionMono: Disposable

    fun unsubscribe() {
        sessionMono.dispose()
        logger.debug("WebSocketSession is disposed: ${sessionMono.isDisposed}")
    }

    fun subscribe() {
        val client: WebSocketClient = ReactorNettyWebSocketClient()

        val sink: Sinks.Many<String> = Sinks.many().multicast().directBestEffort()
        val emitFailureHandler =
            EmitFailureHandler { _: SignalType?, emitResult: EmitResult ->
                emitResult == EmitResult.FAIL_NON_SERIALIZED
            }

        sessionMono = client.execute(uri) { session: WebSocketSession ->
            session.receive()
                .doOnNext { msg: WebSocketMessage ->
                    sink.emitNext(msg.payloadAsText, emitFailureHandler)
                    logger.trace { "MESSAGE: ${msg.payloadAsText}" }
                }
                .then()
        }.subscribe()

        val results: Flux<String> = sink.asFlux()
        results.subscribe(ticksSubscriber)

        logger.debug("WebSocketSession is active: ${!sessionMono.isDisposed}")
    }
}

