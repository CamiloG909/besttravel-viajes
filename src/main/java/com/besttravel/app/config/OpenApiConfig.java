package com.besttravel.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Best Travel API",
		version = "1.0",
		description = "Documentation of my api for endpoints"
	)
)
public class OpenApiConfig {

}
