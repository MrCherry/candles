package com.cherashev.candles.config

import com.cherashev.candles.handler.CandlesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ApplicationConfig {
    @Bean
    fun router(candlesHandler: CandlesHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(
                GET("/start").and(accept(APPLICATION_JSON)),
                candlesHandler::startReceiving
            )
            .andRoute(
                GET("/stop").and(accept(APPLICATION_JSON)),
                candlesHandler::stopReceiving
            )
            .andRoute(
                GET("/candles").and(accept(APPLICATION_JSON)),
                candlesHandler::getAllCandles
            )
    }

    @Bean
    fun redisOperationsSimple(factory: ReactiveRedisConnectionFactory?): ReactiveRedisOperations<String, Double>? {
        val serializer = GenericToStringSerializer(Double::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, Double>(StringRedisSerializer())
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory!!, context)
    }
}