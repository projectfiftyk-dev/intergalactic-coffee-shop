package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public static final String SESSION_COOKIE = "SESSION_ID";

    private final SessionService sessionService;
    private final SecurityContext securityContext;

    public SecurityFilter(
            SessionService sessionService,
            SecurityContext securityContext
    ) {
        this.sessionService = sessionService;
        this.securityContext = securityContext;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String sessionId = extractSessionId(request);

        if (sessionId != null) {
            User user = sessionService.getUserFromSession(sessionId);

            if (user != null) {
                securityContext.setUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (SESSION_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}