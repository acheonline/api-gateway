package ru.achernyavskiy0n.apigateway.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements GatewayFilter {

  private static final Pattern BEARER_PATTERN =
      Pattern.compile("(Bearer) ([A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*)");

  private final JwtTokenService tokenService;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = getToken(exchange.getRequest().getHeaders());

    if (token == null) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    } else {
      try {
        String login = getLoginFromToken(token);
        exchange.mutate().request(request -> request.header("-X-Username", login)).build();

        return chain.filter(exchange);
      } catch (JwtTokenServiceException e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
    }
  }

  private String getToken(HttpHeaders httpHeaders) {
    logger.info("Headers: " + httpHeaders.toString());

    return httpHeaders.entrySet().stream()
        .filter(entry -> entry.getKey().equalsIgnoreCase(HttpHeaders.AUTHORIZATION))
        .flatMap(entry -> entry.getValue().stream())
        .map(
            value -> {
              Matcher matcher = BEARER_PATTERN.matcher(value);
              if (matcher.matches()) {
                return matcher.group(2);
              } else {
                return null;
              }
            })
        .findFirst()
        .orElse(null);
  }

  private String getLoginFromToken(String token) throws JwtTokenServiceException {
    return tokenService.getLogin(token);
  }
}
