package com.project.stockmarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.StockExchangeEntity;

public interface StockExchangeRepository extends JpaRepository<StockExchangeEntity, String>{
	
}
