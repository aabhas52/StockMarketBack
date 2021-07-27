package com.project.stockmarket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.StockExchangeRepository;

@RestController
public class StockExchangeController {

	@Autowired
	private StockExchangeRepository repository;
	
	@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
	@PostMapping("/addStockExchange")
	public void addStockExchange(@RequestBody StockExchangeEntity stockExchange) {
		repository.save(stockExchange);
	}
	
	@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
	@GetMapping("/allStockExchanges")
	public List<StockExchangeEntity> listAllExchanges(){
		return repository.findAll();
	}
}
