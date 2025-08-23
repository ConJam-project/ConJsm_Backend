package com.conjam.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("ConJam Backend API")
                    .description("KOPIS Open API를 활용한 공연 정보 제공 서비스")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("ConJam Backend Team")
                            .email("support@conjam.com")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server"),
                    Server()
                        .url("https://api.conjam.com")
                        .description("Production Server")
                )
            )
    }
}
