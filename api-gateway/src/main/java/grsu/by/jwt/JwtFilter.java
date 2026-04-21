package grsu.by.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(-1)
public class JwtFilter implements GlobalFilter {

    private final JwtHelper jwtHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (path.startsWith("/api/v1/authentication/")) {
            return chain.filter(exchange);
        }

        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("access_token");

        if (cookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = cookie.getValue();

        if (!jwtHelper.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtHelper.extractAllClaims(token);
        String login = claims.getSubject();

        Long profileId = claims.get("profileId", Long.class);
        List<?> rawRoles = claims.get("roles", List.class);
        String roles = rawRoles != null ? String.join(",", rawRoles.stream().map(Object::toString).toList()) : "";

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .headers(h -> {
                    h.remove("X-Auth-Login");
                    h.remove("X-Auth-Profile-Id");
                    h.remove("X-Auth-Roles");
                })
                .header("X-Auth-Login", login)
                .header("X-Auth-Profile-Id", profileId != null ? profileId.toString() : "")
                .header("X-Auth-Roles", roles)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }
}
