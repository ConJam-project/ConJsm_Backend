package com.conjam.backend.config

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.http.codec.xml.Jaxb2XmlEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        val xmlMapper = XmlMapper()
        
        val strategies = ExchangeStrategies.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024) // 1MB
                configurer.defaultCodecs().jaxb2Encoder(Jaxb2XmlEncoder())
                configurer.defaultCodecs().jaxb2Decoder(Jaxb2XmlDecoder())
            }
            .build()

        return WebClient.builder()
            .exchangeStrategies(strategies)
    }
}