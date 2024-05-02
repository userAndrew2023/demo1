package com.example.demo.entity.asset;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.stock.Stock;
import com.example.demo.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Asset extends BaseEntity {

    private Float quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
}
