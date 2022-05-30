package ru.achernyavskiy0n.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.achernyavskiy0n.apigateway.jwt.JwtFilter;

@Configuration
public class RouteConfig {

  @Value("${api-gateway.hosts.news-delivery-host}")
  private String newsDeliveryHost;

  @Value("${api-gateway.hosts.operation-manager-host}")
  private String operationHost;

  @Bean
  public RouteLocator secureProfileRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
    return builder
        .routes()
        .route(
            "news_delivery",
            route ->
                route
                    .path("/api/v1/news/push")
                    .filters(f -> f.filter(jwtFilter))
                    .uri(newsDeliveryHost))
        .route(
            "news_operations",
            route ->
                route
                    .path("/api/v1/operations/**")
                    .filters(f -> f.filter(jwtFilter))
                    .uri(operationHost))
        .build();
  }
}
