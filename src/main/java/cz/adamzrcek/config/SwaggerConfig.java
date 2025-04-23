package cz.adamzrcek.config;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;


@Configuration
@Profile({ "dev", "test"})
@OpenAPIDefinition(
        info = @Info(
                title = "RememberHun Online API",
                version = "1.0",
                description = "API for RememberHun — because love needs documentation ❤️",
                contact = @Contact(name = "Adam", email = "adamzrc@gmail.com")
        ),
        tags = {
                @Tag(name = "auth-controller", description = "Authentication endpoints"),
                @Tag(name = "user-controller", description = "User profile operations"),
                @Tag(name = "wishlist-controller", description = "Wishlist management"),
                @Tag(name = "preference-controller", description = "User preferences"),
                @Tag(name = "important-date-controller", description = "Important dates and events"),
                @Tag(name = "connection-controller", description = "User connections and relationships")
        }
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
