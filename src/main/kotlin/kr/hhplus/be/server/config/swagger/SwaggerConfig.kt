package kr.hhplus.be.server.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("HHPlus API")
                    .version(System.getProperty("app.version") ?: "local")
                    .description("HHPlus 서비스의 OpenAPI 명세")
                    .contact(Contact().name("SHIN HANSOL"))
            )
            .servers(
                listOf(
                    Server().url("http://localhost:8080").description("Local")
                )
            )

}
