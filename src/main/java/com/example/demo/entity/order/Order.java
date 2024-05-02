package com.example.demo.entity.order;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.stock.Stock;
import com.example.demo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    private Float quantity;
    private Float price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderSide side;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
}
