package com.example.demo.service.asset;

import com.example.demo.entity.asset.Asset;
import com.example.demo.repository.asset.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getByUser(Long userId) {
        return this.assetRepository.findByUserId(userId);
    }

    public Asset getById(Long id) {
        return this.assetRepository.findById(id).orElse(null);
    }

    public Asset save() {
        return this.assetRepository.save(null);
    }

    public Asset update() {
        return this.assetRepository.save(null);
    }

    public void delete(Long id) {
        this.assetRepository.deleteById(id);
    }
}