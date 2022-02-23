package ru.achernyavskiy0n.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ProfileRewriteFilter implements GatewayFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String username = usernameFromHeader(exchange.getRequest().getHeaders());
    Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
    String segment = uriVariables.get("username");

    if (segment != null && username != null) {
      if (segment.equalsIgnoreCase(username)) {
        return chain.filter(exchange);
      } else {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
      }
    } else {
      exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
      return exchange.getResponse().setComplete();
    }
  }

  private String usernameFromHeader(HttpHeaders headers) {
    return headers.entrySet().stream()
        .filter(entry -> entry.getKey().equalsIgnoreCase(("-X-Username")))
        .findFirst()
        .map(entry -> entry.getValue().get(0))
        .orElse(null);
  }
}
