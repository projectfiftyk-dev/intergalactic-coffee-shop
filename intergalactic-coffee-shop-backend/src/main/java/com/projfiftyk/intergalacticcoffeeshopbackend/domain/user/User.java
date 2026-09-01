package com.projfiftyk.intergalacticcoffeeshopbackend.domain.user;

import com.projfiftyk.intergalacticcoffeeshopbackend.domain.security.Role;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;

    private Set<Role> roles;

    private String name;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
