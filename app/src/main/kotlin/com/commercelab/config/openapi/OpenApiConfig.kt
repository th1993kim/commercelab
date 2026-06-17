package com.commercelab.config.openapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun commerceLabOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Commerce Lab API")
                    .description("커머스 주문, 재고, 결제 흐름을 실험 및 공부 위한 백엔드 API")
                    .version("v1")
            )
    }
}
