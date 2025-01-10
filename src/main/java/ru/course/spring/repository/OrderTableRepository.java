package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.course.spring.pojo.OrderStatus;
import ru.course.spring.pojo.OrderTable;
import ru.course.spring.pojo.User;

import java.util.List;

public interface OrderTableRepository extends JpaRepository <OrderTable, Long>{

    List<OrderTable> findByOrderTableUser(User user);

    @Query("SELECT o FROM OrderTable o WHERE o.orderTableUser.id = :userId AND o.orderStatus = :status")
    List<OrderTable> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);

    @Query("SELECT o FROM OrderTable o WHERE o.orderTableUser.id = :userId AND o.orderStatus IN :statuses")
    List<OrderTable> findByUserIdAndStatuses(@Param("userId") Long userId, @Param("statuses") List<OrderStatus> statuses);
}
