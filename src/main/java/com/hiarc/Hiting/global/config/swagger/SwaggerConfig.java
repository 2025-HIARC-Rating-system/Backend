package com.hiarc.Hiting.global.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("하이팅 API")
                        .description("구현된 API 테스트")
                        .version("1.0.0"));
    }
}
