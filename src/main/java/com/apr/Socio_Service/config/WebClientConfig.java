package com.apr.Socio_Service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.auth.url:http://auth-service}")
    private String authServiceUrl;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder loadBalancedWebClientBuilder) {
        // Si la URL contiene un punto (ej: .onrender.com o localhost), es una URL directa (DNS/IP).
        // En Render, para evitar que el filtro LoadBalancer cause fallas de resolución de host,
        // creamos un WebClient con Builder limpio y sin la anotación @LoadBalanced.
        if (authServiceUrl.contains(".") || authServiceUrl.contains("localhost")) {
            return WebClient.builder().baseUrl(authServiceUrl).build();
        }
        return loadBalancedWebClientBuilder.baseUrl(authServiceUrl).build();
    }
}
