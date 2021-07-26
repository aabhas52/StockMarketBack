package com.project.stockmarket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.entities.User;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;
import com.project.stockmarket.repositories.UserRepository;

@SpringBootApplication
@EnableJpaRepositories("com.project.stockmarket.repositories")
public class StockMarketApplication implements CommandLineRunner {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	SectorRepository sectorRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	StockExchangeRepository exchangeRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Sector sector = new Sector("Internet Service", "Provide online services to customers");
		CompanyEntity company = new CompanyEntity("Amazon", 1000000, "Jeff Bezos", "", sector, "");
		userRepository.save(new User("aabhas", "password", "aabhasasawa52@gmail.com", "9007720322", true, "admin"));
		userRepository.save(new User("user", "user", "aabhasasawa52@gmail.com", "9007720322", true, "user"));
//		companyRepository.save(company);
//		company = new CompanyEntity("Flipkart", 1000000, "Kalyan Krishnamurthy", "", sector);
//		logger.info(userRepository.findAll().toString());
		exchangeRepository.save(new StockExchangeEntity("BSE", "Bombay Stock Exchange", "", "Mumbai", ""));
		sectorRepository.save(sector);
		companyRepository.save(company);
		logger.info("Companies in sector -> {}", sector.getCompanies());
	}

}
