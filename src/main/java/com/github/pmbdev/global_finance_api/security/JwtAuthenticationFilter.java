package com.github.pmbdev.global_finance_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Get the head Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        //Validate token
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //Extract token and remove "Bearer " (7 digits)
        jwt = authHeader.substring(7);

        //Extract email from the token
        userEmail = jwtUtils.extractUsername(jwt);

        //If there's the email but the user is not authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Load data in the DB
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //If the token is valid
            if(jwtUtils.isTokenValid(jwt, userDetails.getUsername())){

                //Create the authentication details
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                //Details for the request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Seal the token
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        //Continue with the next filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars");
    }
}
