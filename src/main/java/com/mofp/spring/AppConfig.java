package com.mofp.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author rivasyafri
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.mofp")
@ComponentScan(basePackages = "com.mofp")
@EntityScan(basePackages = "com.mofp")
public class AppConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod(RequestMethod.GET.name());
        config.addAllowedMethod(RequestMethod.PUT.name());
        config.addAllowedMethod(RequestMethod.POST.name());
        config.addAllowedMethod(RequestMethod.DELETE.name());
        config.addAllowedMethod(RequestMethod.OPTIONS.name());
        config.addAllowedMethod(RequestMethod.TRACE.name());
        config.addAllowedMethod(RequestMethod.PATCH.name());
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
