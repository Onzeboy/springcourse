package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTable extends JpaRepository<ru.course.spring.pojo.OrderTable, Long> {
}
