package com.project.stockmarket.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.CompanyAndStockExchanges;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.entities.StockPriceEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.CompanyStockExchangesRepository;
import com.project.stockmarket.repositories.StockPriceRepository;

@RestController
@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:3000")
public class StockPriceController {

	@Autowired
	private StockPriceRepository repository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyStockExchangesRepository mappingRepository;

	@PostMapping("/addStockPriceByCode")
	public ResponseEntity<Map<String, String>> addStockPriceByCode(@RequestBody List<Map<String, Object>> stockPrices) {
		Map<String, String> response = new HashMap<String, String>();
		
		if(stockPrices.isEmpty()) {
			response.put("error", "No records");
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
		}
		
		Map<String, Object> sample = stockPrices.get(0);
		String companyCode = (String) sample.get("company_code");
		String exchangeCode = (String) sample.get("stock_exchange_code");
		List<CompanyAndStockExchanges> mappingList = mappingRepository.findByCompanyCodeAndExchange(companyCode,
				exchangeCode);
		
		if(mappingList.isEmpty()) {
			response.put("error", "Company code not found in stock exchange");
			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
		}
		
		CompanyAndStockExchanges mapping = mappingList.get(0);
		CompanyEntity company = mapping.getCompanyEntity();
		StockExchangeEntity exchange = mapping.getStockExchangeEntity();
		
		for(Map<String, Object> stockPrice: stockPrices) {
			double price = ((Number) stockPrice.get("price")).doubleValue();
			String rawDate = (String) stockPrice.get("date");
			String rawTime = (String) stockPrice.get("time");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
			LocalTime time = LocalTime.parse(rawTime);
			LocalDate date = LocalDate.parse(rawDate, formatter);
			StockPriceEntity stock = new StockPriceEntity(company, exchange, price, date, time);
			repository.save(stock);
		}
		response.put("message", Integer.toString(stockPrices.size()));
		response.put("company", company.getCompanyName());
		response.put("exchange", exchange.getStockExchangeName());
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}

	@PostMapping("/findPricesBetweenDates")
	public ResponseEntity<Map<LocalDate, Double>> findPricesBetweenDates(@RequestBody Map<String, Object> body) {
		String companyName = (String) body.get("company_name");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
		String rawStartDate = (String) body.get("start");
		String rawEndDate = (String) body.get("end");
		LocalDate startDate = LocalDate.parse(rawStartDate, formatter);
		LocalDate endDate = LocalDate.parse(rawEndDate, formatter);
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		List<StockPriceEntity> prices = repository.findByCompanyAndDateBetween(company, startDate, endDate);
		Map<LocalDate, Integer> countMap = new HashMap<>();
		Map<LocalDate, Double> priceMap = new TreeMap<>();
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
		return new ResponseEntity<Map<LocalDate, Double>>(priceMap, HttpStatus.OK);
	}

}
