package com.example.demo.users.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    USER;

    private static final String PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return PREFIX + this.name();
    }
}
