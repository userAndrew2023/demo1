package com.example.demo.repository.stock;

import com.example.demo.entity.stock.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {
    Stock findBySymbol(String symbol);
}
