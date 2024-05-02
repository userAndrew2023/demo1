package com.example.demo.repository.order;

import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStockSymbol(String symbol);
    List<Order> findByStockSymbolAndStatus(String symbol, OrderStatus status);
}
