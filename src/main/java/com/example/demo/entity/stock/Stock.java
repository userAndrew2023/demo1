package com.example.demo.entity.stock;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends BaseEntity {

    @Column(unique = true)
    private String symbol;

    @Column(unique = true)
    private String name;
    private String description;
    private String imageCode;

    @OneToMany(mappedBy = "stock")
    private List<Order> orders;

    public Float getPrice() {
        List<Order> marketOrders = this.orders.stream()
                .filter(order -> order.getType().equals(OrderType.MARKET))
                .toList();
        if (marketOrders.isEmpty()) {
            return 0F;
        }
        return marketOrders.get(orders.size() - 1).getPrice();
    }
}
