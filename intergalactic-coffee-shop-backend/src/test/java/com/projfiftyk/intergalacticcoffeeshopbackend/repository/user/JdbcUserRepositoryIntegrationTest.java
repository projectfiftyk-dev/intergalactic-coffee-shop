package com.projfiftyk.intergalacticcoffeeshopbackend.repository.user;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class JdbcUserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUsername() {
        User user = userRepository.findByUsername("admin");

        assertNotNull(user);

        assertEquals("admin", user.getUsername());
        assertEquals("Admin User", user.getName());
        assertEquals("admin-password-hash", user.getPassword());

        assertEquals(
                Set.of(Role.ADMIN),
                user.getRoles()
        );
    }

    @Test
    void shouldFindEmployeeWithEmployeeRole() {
        User user = userRepository.findByUsername("employee");

        assertNotNull(user);

        assertEquals("employee", user.getUsername());
        assertEquals(
                Set.of(Role.EMPLOYEE),
                user.getRoles()
        );
    }

    @Test
    void shouldFindUserWithUserRole() {
        User user = userRepository.findByUsername("john");

        assertNotNull(user);

        assertEquals("john", user.getUsername());
        assertEquals(
                Set.of(Role.USER),
                user.getRoles()
        );
    }

    @Test
    void shouldReturnNullWhenUserDoesNotExist() {
        User user = userRepository.findByUsername("does-not-exist");

        assertNull(user);
    }
}
