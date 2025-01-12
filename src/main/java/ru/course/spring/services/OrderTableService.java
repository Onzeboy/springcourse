package ru.course.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.OrderTable;
import ru.course.spring.repository.OrderTableRepository;

import java.util.List;

@Service
public class OrderTableService {
    @Autowired
    private OrderTableRepository orderTableRepository;

    public List<OrderTable> getAllOrders() {
        return orderTableRepository.findAll();
    }
}
