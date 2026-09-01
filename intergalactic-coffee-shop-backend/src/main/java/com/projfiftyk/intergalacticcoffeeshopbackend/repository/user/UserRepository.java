package com.projfiftyk.intergalacticcoffeeshopbackend.repository.user;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;

public interface UserRepository {
    User findByUsername(String username);

    User create(User user);

    User findById(Long id);
}
