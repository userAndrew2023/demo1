package com.example.demo.repository.asset;

import com.example.demo.entity.asset.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
}
