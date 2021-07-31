package com.project.stockmarket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockPriceRepository;

@RestController
@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:3000")
public class SectorController {
	
	@Autowired
	SectorRepository repository;
	
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	StockPriceRepository priceRepository;
	
	@GetMapping("/allSectors")
	public List<Sector> getAllSectors(){
		return repository.findAll();
	}
}
