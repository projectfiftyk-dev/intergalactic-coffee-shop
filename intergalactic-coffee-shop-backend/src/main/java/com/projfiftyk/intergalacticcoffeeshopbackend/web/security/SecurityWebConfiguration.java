package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityWebConfiguration implements WebMvcConfigurer {

    private final SecurityAuthorizationInterceptor securityAuthorizationInterceptor;

    public SecurityWebConfiguration(
            SecurityAuthorizationInterceptor securityAuthorizationInterceptor
    ) {
        this.securityAuthorizationInterceptor =
                securityAuthorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                securityAuthorizationInterceptor
        );
    }
}