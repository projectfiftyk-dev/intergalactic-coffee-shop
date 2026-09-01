package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;

public interface AuthenticationService {

    User authenticate(String username, String password);
}   