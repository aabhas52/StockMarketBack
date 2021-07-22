package com.project.stockmarket.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.CompanyAndStockExchanges;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.entities.StockPriceEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.CompanyStockExchangesRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;
import com.project.stockmarket.repositories.StockPriceRepository;

@RestController
public class StockPriceController {

	@Autowired
	private StockPriceRepository repository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StockExchangeRepository exchangeRepository;

	@Autowired
	private CompanyStockExchangesRepository mappingRepository;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/addStockPrice")
	public void addStockPrice(@RequestBody List<Map<String, Object>> stockPrices) {
		for(Map<String, Object> stockPrice: stockPrices) {
			String companyName = (String) stockPrice.get("company_name");
			String exchangeCode = (String) stockPrice.get("stock_exchange_code");
			double price = ((Number) stockPrice.get("price")).doubleValue();
			String rawDate = (String) stockPrice.get("date");
			String rawTime = (String) stockPrice.get("time");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y");
			LocalDate date = LocalDate.parse(rawDate, formatter);
			LocalTime time = LocalTime.parse(rawTime);
			CompanyEntity company = companyRepository.findByCompanyName(companyName);
			Optional<StockExchangeEntity> exchange = exchangeRepository.findById(exchangeCode);
			StockPriceEntity stock = new StockPriceEntity(company, exchange.get(), price, date, time);
			repository.save(stock);
		}
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{​​​​​​​id}​​​​​​​").buildAndExpand(stock.getId())
//	            .toUri();
//	    
//	    return ResponseEntity.created(location).build();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/addStockPriceByCode")
	public void addStockPriceByCode(@RequestBody List<Map<String, Object>> stockPrices) {
		for(Map<String, Object> stockPrice: stockPrices) {
			String companyCode = (String) stockPrice.get("company_code");
			String exchangeCode = (String) stockPrice.get("stock_exchange_code");
			double price = ((Number) stockPrice.get("price")).doubleValue();
//			String date = (String) stockPrice.get("date");
//			String time = (String) stockPrice.get("time");
			String rawDate = (String) stockPrice.get("date");
			String rawTime = (String) stockPrice.get("time");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
			LocalTime time = LocalTime.parse(rawTime);
			LocalDate date = LocalDate.parse(rawDate, formatter);
			List<CompanyAndStockExchanges> mappingList = mappingRepository.findByCompanyCodeAndExchange(companyCode,
					exchangeCode);
			CompanyAndStockExchanges mapping = mappingList.get(0);
			CompanyEntity company = mapping.getCompanyEntity();
			StockExchangeEntity exchange = mapping.getStockExchangeEntity();
			StockPriceEntity stock = new StockPriceEntity(company, exchange, price, date, time);
			repository.save(stock);
		}
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{​​​​​​​id}​​​​​​​")
//				.buildAndExpand(stock.getId()).toUri();
//
//		return ResponseEntity.created(location).build();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/findPricesById/{id}")
	public List<StockPriceEntity> findCompanyPricesById(@PathVariable("id") Long companyId) {
		Optional<CompanyEntity> company = companyRepository.findById(companyId);
		return repository.findByCompany(company.get());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/findPricesByCompany/{name}")
	public List<StockPriceEntity> findCompanyPrices(@PathVariable("name") String companyName) {
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		return repository.findByCompany(company);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/findPricesByExchange/{exchange}")
	public List<StockPriceEntity> findExchangePrices(@PathVariable("exchange") String exchangeName) {
		Optional<StockExchangeEntity> exchange = exchangeRepository.findById(exchangeName);
		return repository.findByStockExchange(exchange.get());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/findPricesBetweenDates")
	public List<StockPriceEntity> findPricesBetweenDates(@RequestBody Map<String, Object> body) {
		String companyName = (String) body.get("company_name");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
		String rawStartDate = (String) body.get("start");
		String rawEndDate = (String) body.get("end");
		LocalDate startDate = LocalDate.parse(rawStartDate, formatter);
		LocalDate endDate = LocalDate.parse(rawEndDate, formatter);
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		return repository.findByCompanyAndDateBetween(company, startDate, endDate);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getPrices")
	public List<StockPriceEntity> findAllPrices() {
		return repository.findAll();
	}
}
