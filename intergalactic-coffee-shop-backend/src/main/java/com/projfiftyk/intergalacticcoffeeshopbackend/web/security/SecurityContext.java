package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class SecurityContext {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public boolean hasRole(Role role) {
        return user != null && user.getRoles().contains(role);
    }

    public void clear() {
        user = null;
    }
}