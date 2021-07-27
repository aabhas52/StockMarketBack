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

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.IpoDetailEntity;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.IpoDetailRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;

@RestController
public class IpoDetailsController {

	@Autowired
	private IpoDetailRepository repository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StockExchangeRepository exchangeRepository;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/companyIpo/{name}")
	public List<IpoDetailEntity> listByCompany(@PathVariable("name") String companyName) {
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		return repository.findByCompanyOrderByOpeningDateDesc(company);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/editIpo")
	public void editIpo(@RequestBody Map<String, Object> ipoDetails) {
		String companyName = (String) ipoDetails.get("company_name");
		String exchangeCode = (String) ipoDetails.get("stock_exchange_code");
		double price = ((Number) ipoDetails.get("price")).doubleValue();
		String remarks = (String) ipoDetails.get("remarks");
		long shares = ((Number) ipoDetails.get("shares")).longValue();
		String rawDate = (String) ipoDetails.get("date");
		String rawTime = (String) ipoDetails.get("time");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
		LocalDate date = LocalDate.parse(rawDate, formatter);
		LocalTime time = LocalTime.parse(rawTime);
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		Optional<StockExchangeEntity> exchange = exchangeRepository.findById(exchangeCode);
		Optional<IpoDetailEntity> ipo = repository.findByCompanyAndStockExchange(company, exchange.get());
		if (ipo.isPresent()) {
			repository.delete(ipo.get());
		}
		IpoDetailEntity newIpo = new IpoDetailEntity(company, exchange.get(), price, shares, date, time, remarks);
		repository.save(newIpo);
	}

	@CrossOrigin("http://localhost:3000")
	@GetMapping("/allIPOs")
	public List<IpoDetailEntity> allIpo() {
		return repository.findAllByOrderByOpeningDateDesc();
	}
}
