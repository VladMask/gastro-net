package grsu.by.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
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

    private static final List<String> PUBLIC_GET_PREFIXES = List.of(
            "/api/v1/meals",
            "/api/v1/meal-categories",
            "/api/v1/reviews",
            "/api/v1/restaurants/search"
    );

    private static final List<String> PUBLIC_ANY_METHOD = List.of(
            "/api/v1/authentication/login",
            "/api/v1/authentication/register",
            "/api/v1/authentication/refresh",
            "/api/v1/authentication/logout",
            "/api/v1/restaurants/apply"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();

        boolean isPublicAny = PUBLIC_ANY_METHOD.stream().anyMatch(p ->
                path.equals(p) || path.startsWith(p + "/") || path.startsWith(p + "?")
        );

        boolean isPublicGet = method == HttpMethod.GET &&
                PUBLIC_GET_PREFIXES.stream().anyMatch(p ->
                        path.equals(p) || path.startsWith(p + "/") || path.startsWith(p + "?")
                );

        boolean isPublicRestaurant = method == HttpMethod.GET && (
                path.equals("/api/v1/restaurants") ||
                        path.matches("/api/v1/restaurants/\\d+")
        );

        if (isPublicAny || isPublicGet || isPublicRestaurant) return chain.filter(exchange);

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