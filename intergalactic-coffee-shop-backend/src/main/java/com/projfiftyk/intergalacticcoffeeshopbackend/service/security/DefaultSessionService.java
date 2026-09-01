package com.projfiftyk.intergalacticcoffeeshopbackend.service.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Session;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.security.SessionRepository;
import com.projfiftyk.intergalacticcoffeeshopbackend.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class DefaultSessionService implements SessionService {

    private static final int SESSION_DURATION_HOURS = 24;
    private static final int SESSION_ID_LENGTH = 32;

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public DefaultSessionService(
            SessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Session createSession(User user) {
        Session session = new Session();

        session.setId(generateSessionId());
        session.setUserId(user.getId());
        session.setExpiresAt(
                LocalDateTime.now()
                        .plusHours(SESSION_DURATION_HOURS)
        );

        return sessionRepository.create(session);
    }

    @Override
    public User getUserFromSession(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return null;
        }

        Session session = sessionRepository.findById(sessionId);

        if (session == null) {
            return null;
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            sessionRepository.deleteById(sessionId);
            return null;
        }

        return userRepository.findById(session.getUserId());
    }

    @Override
    public void invalidateSession(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return;
        }

        sessionRepository.deleteById(sessionId);
    }

    private String generateSessionId() {
        byte[] bytes = new byte[SESSION_ID_LENGTH];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}