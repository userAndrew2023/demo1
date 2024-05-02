package com.example.demo.service;

import com.example.demo.entity.asset.Asset;
import com.example.demo.repository.asset.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAll() {
        List<Asset> assetList = new ArrayList<>();
        this.assetRepository.findAll().forEach(assetList::add);

        return assetList;
    }
}