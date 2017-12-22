package com.bkavramlari.financialaudit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    private RelaxedPropertyResolver propertyResolver;

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "TODO Project REST API",
                "Spring Boot REST API for Online Store",
                "1.0",
                "Terms of service",
                new Contact("Benan Aktaş", "http://www.pusulait.com", "benan.aktas@pusulait.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }

    /**
     * Swagger Springfox configuration.le
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN)) // and by paths
                .build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }


}