package com.project.stockmarket.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.CompanyStockExchangesRepository;

@RestController
public class CompanyStockExchangeController {

	@Autowired
	CompanyStockExchangesRepository repository;
	
	@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
	@PostMapping("/mapStockExchanges")
	public void mapCompanyExchanges(@RequestBody Map<String, Object> body) {
		String companyName = (String) body.get("company_name");
		List<Map<String, String>> companyCodes =  (List<Map<String, String>>) body.get("company_codes");
		repository.addMapping(companyName, companyCodes);
	}
	
	@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
	@GetMapping("/exchangesToCompany/{name}")
	public List<StockExchangeEntity> findStockExchangesForCompany(@PathVariable("name") String name){
		return repository.findStockExchangesForCompany(name);
	}
	
	@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
	@GetMapping("/deactivateCompany/{companyName}")
	public void deactivateCompany(@PathVariable("companyName") String companyName) {
		repository.deactivateCompany(companyName);
	}
}
