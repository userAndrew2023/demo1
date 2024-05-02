package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseEntity {

    private String username;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Asset> assets;
}
