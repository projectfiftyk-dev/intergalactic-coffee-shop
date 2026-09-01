package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.ForbiddenException;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityAuthorizationInterceptor implements HandlerInterceptor {

    private final SecurityContext securityContext;

    public SecurityAuthorizationInterceptor(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequireRole requireRole =
                handlerMethod.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            return true;
        }

        if (!securityContext.isAuthenticated()) {
            throw new UnauthorizedException(
                    "Authentication required"
            );
        }

        Role requiredRole = requireRole.value();

        if (!securityContext.hasRole(requiredRole)) {
            throw new ForbiddenException(
                    "Insufficient permissions"
            );
        }

        return true;
    }
}