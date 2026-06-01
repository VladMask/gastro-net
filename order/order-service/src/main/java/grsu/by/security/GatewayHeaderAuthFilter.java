package grsu.by.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GatewayHeaderAuthFilter extends OncePerRequestFilter {

    private static final String HEADER_LOGIN = "X-Auth-Login";
    private static final String HEADER_PROFILE_ID = "X-Auth-Profile-Id";
    private static final String HEADER_ROLES = "X-Auth-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String email = request.getHeader(HEADER_LOGIN);
        String profileIdStr = request.getHeader(HEADER_PROFILE_ID);
        String rolesStr = request.getHeader(HEADER_ROLES);
        if (email != null && profileIdStr != null) {
            Long profileId = parseLong(profileIdStr);
            List<SimpleGrantedAuthority> authorities = parseRoles(rolesStr);
            ProfilePrincipal principal = new ProfilePrincipal(profileId, email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Long parseLong(String value) {
        try { return Long.parseLong(value); }
        catch (NumberFormatException e) { return null; }
    }

    private List<SimpleGrantedAuthority> parseRoles(String rolesStr) {
        if (rolesStr == null || rolesStr.isBlank()) return Collections.emptyList();
        return Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}