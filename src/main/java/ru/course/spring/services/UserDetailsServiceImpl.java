package ru.course.spring.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByUserEmail(email)
                .map(user -> {
                    if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
                        throw new IllegalArgumentException("Email cannot be null or empty");
                    }
                    if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
                        throw new IllegalArgumentException("Password cannot be null or empty");
                    }
                    if (user.getRole() == null) {
                        throw new IllegalArgumentException("Role cannot be null or empty");
                    }
                    return new org.springframework.security.core.userdetails.User(
                            user.getUserEmail(),
                            user.getUserPassword(),
                            Collections.singleton(user.getRole())
                    );
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException("Невозможно найти пользователя " + email));
    }
}
