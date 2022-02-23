package ru.achernyavskiy0n.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.achernyavskiy0n.apigateway.jwt.JwtFilter;
import ru.achernyavskiy0n.apigateway.security.MyProfileRewriteFilter;
import ru.achernyavskiy0n.apigateway.security.ProfileRewriteFilter;

import static ru.achernyavskiy0n.apigateway.utils.ConfigUtils.ROOT_PATH;

@Configuration
public class RouteConfig {

  @Value("${identityServiceUri}")
  private String identityService;

  @Bean
  public RouteLocator noSecureRouteLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            "registration",
            route ->
                route
                    .path("/registration")
                    .filters(f -> f.rewritePath("/registration", ROOT_PATH.concat("/registration")))
                    .uri(identityService))
        .route(
            "login",
            route ->
                route
                    .path("/login")
                    .filters(f -> f.rewritePath("/login", ROOT_PATH.concat("/login")))
                    .uri(identityService))
        .build();
  }

  @Bean
  public RouteLocator secureProfileRouteLocator(
      RouteLocatorBuilder builder, JwtFilter jwtFilter, ProfileRewriteFilter profileRewriteFilter) {
    return builder
        .routes()
        .route(
            "user_profile",
            route ->
                route
                    .path("/profile/{username}")
                    .filters(
                        f ->
                            f.filter(jwtFilter)
                                .rewritePath(
                                    "/profile/(?<segment>.*)", ROOT_PATH.concat("/${segment}"))
                                .filter(profileRewriteFilter))
                    .uri(identityService))
        .route(
            "user_profile_edit",
            route ->
                route
                    .path("/profile/{username}/update")
                    .filters(
                        f ->
                            f.filter(jwtFilter)
                                .rewritePath(
                                    "/profile/(?<segment>.*)/update",
                                    ROOT_PATH.concat("/${segment}/update"))
                                .filter(profileRewriteFilter))
                    .uri(identityService))
        .build();
  }

  @Bean
  public RouteLocator secureMyProfileRouteLocator(
      RouteLocatorBuilder builder,
      JwtFilter jwtFilter,
      MyProfileRewriteFilter myProfileRewriteFilter) {
    return builder
        .routes()
        .route(
            "my_profile",
            route ->
                route
                    .path("/me")
                    .filters(
                        f ->
                            f.filter(jwtFilter)
                                .rewritePath("/me", ROOT_PATH)
                                .filter(myProfileRewriteFilter))
                    .uri(identityService))
        .build();
  }
}
