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
@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:3000")
public class StockExchangeController {

	@Autowired
	private StockExchangeRepository repository;
	
	@PostMapping("/addStockExchange")
	public void addStockExchange(@RequestBody StockExchangeEntity stockExchange) {
		repository.save(stockExchange);
	}
	
	@GetMapping("/allStockExchanges")
	public List<StockExchangeEntity> listAllExchanges(){
		return repository.findAll();
	}
}
