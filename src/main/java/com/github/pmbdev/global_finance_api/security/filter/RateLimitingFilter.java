package com.github.pmbdev.global_finance_api.security.filter;

import com.github.pmbdev.global_finance_api.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;

    public RateLimitingFilter(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get client IP
        String ip = request.getRemoteAddr();
        var bucket = rateLimitingService.resolveBucket(ip);

        // Try to consume the bucket token
        if (bucket.tryConsume(1)) {
            // If there are still tokens, filter the request
            //System.out.println(">>> Petición permitida. Tokens restantes: " + bucket.getAvailableTokens());
            filterChain.doFilter(request, response);
        } else {
            // If there's no more tokens, return error 429
            //System.out.println("XXX BLOQUEADO por Rate Limit - IP: " + ip);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }
}