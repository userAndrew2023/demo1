package com.example.demo.repository.asset;

import com.example.demo.entity.asset.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
    List<Asset> findByUserId(Long userId);
}
