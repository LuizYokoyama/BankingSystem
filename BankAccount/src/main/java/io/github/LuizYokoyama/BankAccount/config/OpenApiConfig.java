package io.github.LuizYokoyama.BankAccount.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Bank Account API")
                        .description("A service providing Bank Account operations.")
                        .version("Playground 0.1"));
    }

}
