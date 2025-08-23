package com.conjam.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConJamBackendApplication

fun main(args: Array<String>) {
    runApplication<ConJamBackendApplication>(*args)
}