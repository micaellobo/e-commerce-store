package com.example.userservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final ContextHolder contextHolder;

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors()
                .add(new RestTemplateInterceptor(this.contextHolder));
        return restTemplate;
    }

}
