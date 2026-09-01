package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Session;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;

public interface SessionService {

    Session createSession(User user);

    User getUserFromSession(String sessionId);

    void invalidateSession(String sessionId);
}