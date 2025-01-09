package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.spring.pojo.OrderTable;
import ru.course.spring.pojo.User;

import java.util.List;

public interface OrderTableRepository extends JpaRepository <OrderTable, Long>{

    List<OrderTable> findByOrderTableUser(User user);
}
