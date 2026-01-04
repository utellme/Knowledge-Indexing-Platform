package com.myproject.knowledge.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myproject.knowledge.context.SecurityContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    /**
     * Simplified API key store.
     * In production: Vault, database, IAM, etc.
     */
    private static final Map<String, String> API_KEYS = Map.of(
        "tenant-a-key", "tenant-a",
        "tenant-b-key", "tenant-b"
    );

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-Key");

        if (apiKey == null || !API_KEYS.containsKey(apiKey)) {
            response.sendError(
                HttpStatus.UNAUTHORIZED.value(),
                "Missing or invalid API key"
            );
            return;
        }

        // Bind tenant to request context
        SecurityContext.setTenantId(API_KEYS.get(apiKey));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TENANT"));

        // Mark request as authenticated
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "api-key-user",   // principal
                        null,             // credentials
                        // Collections.emptyList() // no authorities
                        authorities // with role tenant authority
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContext.clear();
        }
    }

    /**
     * Exclude health checks from authentication if desired.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return (path.startsWith("/api/v1/health") || path.startsWith("/api/v1/metrics")
               || path.startsWith("/actuator") || path.startsWith("/swagger-ui")
               || path.startsWith("/v3/api-docs") || path.startsWith("/h2-console")
            );
    }
}

