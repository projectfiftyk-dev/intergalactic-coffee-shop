package com.projfiftyk.intergalacticcoffeeshopbackend.web.security;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {

    Role value();
}