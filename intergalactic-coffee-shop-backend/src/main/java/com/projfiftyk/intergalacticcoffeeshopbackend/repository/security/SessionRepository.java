package com.projfiftyk.intergalacticcoffeeshopbackend.repository.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Session;

public interface SessionRepository {

    Session create(Session session);

    Session findById(String id);

    void deleteById(String id);
}