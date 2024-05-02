package com.example.demo.service.stock;

import com.example.demo.entity.stock.Stock;
import com.example.demo.repository.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAll() {
        List<Stock> list = new ArrayList<>();
        this.stockRepository.findAll().forEach(list::add);

        return list;
    }

    public Stock getById(Long id) {
        return this.stockRepository.findById(id).orElse(null);
    }

    public Stock getBySymbol(String symbol) {
        return this.stockRepository.findBySymbol(symbol);
    }

    public Stock create() {
        return this.stockRepository.save(null);
    }

    public Stock update() {
        return this.stockRepository.save(null);
    }

    public void delete(Long id) {
        this.stockRepository.deleteById(id);
    }
}