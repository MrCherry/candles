package com.cherashev.candles

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CandlesApplication

fun main(args: Array<String>) {
    runApplication<CandlesApplication>(*args)
}
