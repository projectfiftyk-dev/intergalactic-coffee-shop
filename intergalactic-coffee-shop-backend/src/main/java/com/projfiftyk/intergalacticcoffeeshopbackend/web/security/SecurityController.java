package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Session;
import com.projfiftyk.intergalacticcoffeeshopbackend.domain.user.User;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.AuthenticationService;
import com.projfiftyk.intergalacticcoffeeshopbackend.service.security.SessionService;
import com.projfiftyk.intergalacticcoffeeshopbackend.transfer.security.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;

    public SecurityController(
            AuthenticationService authenticationService,
            SessionService sessionService
    ) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        User user = authenticationService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        Session session = sessionService.createSession(user);

        ResponseCookie cookie = ResponseCookie
                .from(SecurityFilter.SESSION_COOKIE, session.getId())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofHours(24))
                .build();

        response.addHeader(
                "Set-Cookie",
                cookie.toString()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(
                    value = SecurityFilter.SESSION_COOKIE,
                    required = false
            )
            String sessionId,
            HttpServletResponse response
    ) {
        sessionService.invalidateSession(sessionId);

        ResponseCookie cookie = ResponseCookie
                .from(SecurityFilter.SESSION_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(
                "Set-Cookie",
                cookie.toString()
        );

        return ResponseEntity.noContent().build();
    }
}