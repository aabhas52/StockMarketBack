package com.project.stockmarket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
	
//	@Autowired
//	private PasswordEncoder bcryptEncoder;

//	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		Sector sector1 = new Sector("Internet Service", "Provide online services to customers");
//		Sector sector2 = new Sector("Finance", "Provide financial services to commercial and retail customers");
//		Sector sector3 = new Sector("Healthcare", "Provide medical services");
//		Sector sector4 = new Sector("Pharmaceutical", "Manufacture and market medicines");
//		Sector sector5 = new Sector("Hotels", "Provide lodging");
//		userRepository.save(new User("admin", bcryptEncoder.encode("admin"), "", "", true, "admin"));
//		userRepository.save(new User("user", bcryptEncoder.encode("user"), "", "", true, "user"));
//		exchangeRepository.save(new StockExchangeEntity("BSE", "Bombay Stock Exchange", "Asia's oldest stock exchange", "Dalal Street, Mumbai", ""));
//		exchangeRepository.save(new StockExchangeEntity("NSE", "National Stock Exchange", "Leading stock exchange of India", "Bandra, Mumbai", ""));
//		sectorRepository.save(sector1);
//		sectorRepository.save(sector2);
//		sectorRepository.save(sector3);
//		sectorRepository.save(sector4);
//		sectorRepository.save(sector5);
	}

}
