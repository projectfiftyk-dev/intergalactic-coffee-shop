package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import com.projfiftyk.intergalacticcoffeeshopbackend.error.security.AuthenticationException;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public DefaultAuthenticationService(
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException("Invalid username or password");
        }

        if (!passwordHasher.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return user;
    }
}