package by.skilsoft.jh.courses.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

@Primary
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
@Configuration
public class OpenApiConfiguration {

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        List<SwaggerResource> resources = new ArrayList<>();
        SwaggerResource wsResource = new SwaggerResource();
        wsResource.setName("Documentation");
        wsResource.setSwaggerVersion("2.0");
        wsResource.setUrl("/v2/api-docs?group=Documentation");
        resources.add(wsResource);
        return () -> resources;
    }

    @Bean
    public Docket apiFirstDocket(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.ApiDocs properties = jHipsterProperties.getApiDocs();
        Contact contact = new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
            "API First " + properties.getTitle(),
            properties.getDescription(),
            properties.getVersion(),
            properties.getTermsOfServiceUrl(),
            contact,
            properties.getLicense(),
            properties.getLicenseUrl(),
            new ArrayList<>()
        );

        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Documentation")
            .forCodeGeneration(true)
            .pathMapping("/")
            .host("localhost")
            .forCodeGeneration(true)
            .servers(new Server("dev", "localhost/", "", new ArrayList<>(), new ArrayList<>()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("by.skilsoft.jh.courses.web.rest"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo);
    }
}
