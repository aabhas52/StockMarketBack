package com.project.stockmarket.controllers;

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
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.SectorRepository;

@RestController
@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:3000")
public class CompanyController {

	@Autowired
	private CompanyRepository repository;

	@Autowired
	private SectorRepository sectorRepository;

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

	@PostMapping("/editCompany")
	public ResponseEntity<String> editCompany(@RequestBody CompanyEntity company) {
		repository.save(company);
		return new ResponseEntity<String>("Company Edited", HttpStatus.OK);
	}

	@GetMapping("/allCompanies")
	public ResponseEntity<List<CompanyEntity>> allCompanies() {
		List<CompanyEntity> companies = repository.findAll();
		return new ResponseEntity<List<CompanyEntity>>(companies, HttpStatus.OK);
	}

	@GetMapping("/allCompanies/{sector}")
	public ResponseEntity<List<CompanyEntity>> allCompaniesBySector(@PathVariable("sector") String sectorName) {
		Optional<Sector> sector = sectorRepository.findBySectorName(sectorName);
		List<CompanyEntity> companies = repository.findAllBySector(sector.get());
		return new ResponseEntity<List<CompanyEntity>>(companies, HttpStatus.OK);
	}
	
	@GetMapping("/findMatchingCompany/{text}")
	public ResponseEntity<List<CompanyEntity>> findMatchingCompany(@PathVariable("text") String text){
		return new ResponseEntity<List<CompanyEntity>>(repository.findByCompanyNameContainingIgnoreCase(text), HttpStatus.OK);
	}
}
