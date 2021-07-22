package com.project.stockmarket.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CompanyController {

	@Autowired
	private CompanyRepository repository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private StockPriceRepository priceRepository;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/addCompany")
	public ResponseEntity<String> addCompany(@RequestBody Map<String, Object> companyDetails) {
		String companyName = (String) companyDetails.get("company_name");
		double turnover = ((Number) companyDetails.get("turnover")).doubleValue();
		String ceo = (String) companyDetails.get("ceo");
		String directors = (String) companyDetails.get("directors");
		String writeup = (String) companyDetails.get("writeup");
		String sectorName = (String) companyDetails.get("sector");
		Optional<Sector> sector = sectorRepository.findBySectorName(sectorName);
		
		if (sector.isPresent()) {
			CompanyEntity company = new CompanyEntity(companyName, turnover, ceo, writeup, sector.get(), directors);
			repository.save(company);
			return new ResponseEntity<String>("Company Added", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Sector not defined", HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/editCompany")
	public ResponseEntity<String> editCompany(@RequestBody CompanyEntity company) {
		repository.save(company);
		return new ResponseEntity<String>("Company Edited", HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/allCompanies")
	public ResponseEntity<List<CompanyEntity>> allCompanies() {
		List<CompanyEntity> companies = repository.findAll();
		return new ResponseEntity<List<CompanyEntity>>(companies, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/allCompanies/{sector}")
	public ResponseEntity<List<CompanyEntity>> allCompaniesBySector(@PathVariable("sector") String sectorName) {
		Optional<Sector> sector = sectorRepository.findBySectorName(sectorName);
		List<CompanyEntity> companies = repository.findAllBySector(sector.get());
		return new ResponseEntity<List<CompanyEntity>>(companies, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("findCompany/{name}")
	public ResponseEntity<Map<String, Object>> findCompany(@PathVariable("name") String companyName) {
		CompanyEntity company = repository.findByCompanyName(companyName);
		StockPriceEntity latestPrice = priceRepository.findTop1ByCompanyOrderByDateDesc(company);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("company", company);
		response.put("price", latestPrice);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("findMatchingCompanies/{text}")
	public List<CompanyEntity> findMatchingCompanies(@PathVariable String text) {
		return repository.findByCompanyNameContaining(text);
	}
}
