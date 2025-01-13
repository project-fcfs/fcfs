package hanghae.user_service.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "User Service API specifications for MSA",
        description = "User Service API specifications with spring boot 3.4 + spring cloud",
        version = "v1.0.0")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/users/**"};

        return GroupedOpenApi.builder()
                .group("일반 사용자를 위한 User 도메인에 대한 API")
                .pathsToMatch(paths)
                .build();
    }
}
