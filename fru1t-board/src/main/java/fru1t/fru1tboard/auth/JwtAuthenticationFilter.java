package fru1t.fru1tboard.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil = new JwtUtil();

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/signup", "/api/v1/login"
    );

    private static final List<String> REFRESH_TOKEN_PATHS = List.of(
            "/api/v1/refresh"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            log.info("Skipping authentication for GET request: {}", path);
            return true;
        }

        if ("POST".equalsIgnoreCase(method) && EXCLUDED_PATHS.contains(path)) {
            log.info("Skipping authentication for whitelisted POST request: {}", path);
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Executing JWT authentication filter for request: {} {}", request.getMethod(), request.getRequestURI());

        String token = extractToken(request);
        String path = request.getRequestURI();

        if (token == null) {
            log.info("No JWT token found in request");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        log.debug("Extracted JWT token: {}", token);

        if (REFRESH_TOKEN_PATHS.contains(path)) {
            if (!jwtUtil.validateTokenByType(token, "refresh")) {
                log.info("Invalid Refresh JWT token provided");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        } else {
            if (!jwtUtil.validateTokenByType(token, "access")) {
                log.info("Invalid Access JWT token provided");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        }

        log.debug("JWT token is valid. Proceeding with request.");
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            log.debug("Authorization header found: {}", header);
            return header.substring(7);
        }
        log.debug("No Authorization header found in request");
        return null;
    }
}
