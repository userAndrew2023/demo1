package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Asset extends BaseEntity {

    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
