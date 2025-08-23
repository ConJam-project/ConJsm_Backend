package com.conjam.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        // Spring Boot가 jackson-dataformat-xml 의존성을 발견하면
        // 자동으로 Jackson XML 코덱을 설정하므로 명시적 설정 불필요
        val strategies = ExchangeStrategies.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024) // 1MB
                // JAXB 코덱 제거 - Jackson XML이 자동으로 사용됨
            }
            .build()

        return WebClient.builder()
            .exchangeStrategies(strategies)
    }
}