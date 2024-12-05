package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.spring.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
