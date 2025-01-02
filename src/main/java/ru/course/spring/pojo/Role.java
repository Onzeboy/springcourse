package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Table(name = "roles")
public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    @Override public String getAuthority() {
        return name();
    }
}
