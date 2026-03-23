package com.juniverse.docprocessor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDED_PATHS = {
            "/error",
            "/actuator/**",
            "/favicon.ico"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimingInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDED_PATHS)
                .order(1);

        registry.addInterceptor(new ApiKeyInterceptor())
                .addPathPatterns("/jobs/**")
                .excludePathPatterns(EXCLUDED_PATHS)
                .order(2);}
}