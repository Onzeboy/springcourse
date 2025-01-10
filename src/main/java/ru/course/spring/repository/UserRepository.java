package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import ru.course.spring.pojo.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);

    @Query("SELECT u from User u where u.userEmail = :email")
    User findByUserToEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.userEmail = :userEmail")
    Optional<User> findByUserEmailWithRole(@Param("email") String userEmail);

    @Query("SELECT u FROM User u WHERE u.id = :userID")
    User getUserById(@Param("userID") Long userID);

    List<User> findAllByIdNot(Long id);

    Optional<User> findByUserPhoneNumber(String phoneNumber);
}
