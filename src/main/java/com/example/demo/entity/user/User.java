package com.example.demo.entity.user;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.asset.Asset;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Asset> assets;
}
