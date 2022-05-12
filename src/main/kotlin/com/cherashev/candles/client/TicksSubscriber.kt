package com.cherashev.candles.client

import mu.KotlinLogging
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.stereotype.Component

@Component
class TicksSubscriber(
    val ticksProcessor: TicksProcessor
) : Subscriber<String?> {
    private val logger = KotlinLogging.logger {}

    override fun onSubscribe(s: Subscription) {
        s.request(Long.MAX_VALUE)
    }

    override fun onNext(payload: String?) {
        logger.trace("Message from WebSocket: $payload")
        payload?.let { ticksProcessor.processRawTick(it) }
    }

    override fun onError(t: Throwable?) {}
    override fun onComplete() {}
}
