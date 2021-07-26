package com.project.stockmarket.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.entities.StockPriceEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockPriceRepository;

@RestController
public class SectorController {
	
	@Autowired
	SectorRepository repository;
	
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	StockPriceRepository priceRepository;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/allSectors")
	public List<Sector> getAllSectors(){
		return repository.findAll();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/sectorPrices")
	public ResponseEntity<Map<LocalDate, Double>> findSectorPrices(@RequestBody Map<String, Object> body){
		String sectorName = (String) body.get("sector");
		String rawStartDate = (String) body.get("start");
		String rawEndDate = (String) body.get("end");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
		LocalDate startDate = LocalDate.parse(rawStartDate, formatter);
		LocalDate endDate = LocalDate.parse(rawEndDate, formatter);
		Optional<Sector> sector = repository.findBySectorName(sectorName);
		List<StockPriceEntity> prices = new ArrayList<>();
		Map<LocalDate, Double> priceMap = new TreeMap<>();
		if(sector.isEmpty()) {
			return new ResponseEntity<Map<LocalDate, Double>>(HttpStatus.BAD_REQUEST);
		}
		else {
			List<CompanyEntity> companies = companyRepository.findAllBySector(sector.get());
			for(CompanyEntity company: companies) {
				List<StockPriceEntity> companyPrices = priceRepository.findByCompanyAndDateBetween(company, startDate, endDate);
				prices.addAll(companyPrices);
			}
			Map<LocalDate, Integer> countMap = new HashMap<>();
			for(StockPriceEntity price: prices) {
				LocalDate date = price.getDate();
				double accPrice = priceMap.containsKey(date) ? priceMap.get(date) : 0;
				int count = countMap.containsKey(date) ? countMap.get(date) : 0;
				accPrice += price.getCurrentPrice();
				count++;
				priceMap.put(date, accPrice);
				countMap.put(date, count);
			}
			for(LocalDate date: priceMap.keySet()) {
				double accPrice = priceMap.get(date);
				int count = countMap.get(date);
				priceMap.put(date, accPrice / count);
			}
		}
		return new ResponseEntity<Map<LocalDate, Double>>(priceMap, HttpStatus.OK);
	}
}
