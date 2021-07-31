package com.project.stockmarket.testClasses;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.CompanyStockExchangesRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class CompanyStockExchangesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StockExchangeRepository exchangeRepository;

	@Autowired
	private CompanyStockExchangesRepository mappingRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void loadData() {
		Sector sector = new Sector("Internet Service", "Provide online services to people");
		sectorRepository.save(sector);

		CompanyEntity company1 = new CompanyEntity("Amazon", 100, "Jeff Bezos", "Online Shopping", sector,
				"Andy Jassy");
		companyRepository.save(company1);
		CompanyEntity company2 = new CompanyEntity("Flipkart", 200, "Sachin Bansal", "Online Shopping for India",
				sector, "Kalyan Krishnamurthy");
		companyRepository.save(company2);
		CompanyEntity company3 = new CompanyEntity("HDFC", 300, "Sashidhar", "Banking solutions", sector, " None");
		companyRepository.save(company3);

		StockExchangeEntity exchange1 = new StockExchangeEntity("BSE", "Bombay Stock Exchange",
				"Asia's oldest stock exchange", "Dalal Street, Mumbai", "");
		exchangeRepository.save(exchange1);
		StockExchangeEntity exchange2 = new StockExchangeEntity("NSE", "National Stock Exchange",
				"Leading stock exchange of India", "Bandra, Mumbai", "");
		exchangeRepository.save(exchange2);

		Map<String, Object> mappingBody = new HashMap<>();
		mappingBody.put("company_name", "Flipkart");
		List<Map<String, String>> codes = new ArrayList<>();
		Map<String, String> mappingBSE = new HashMap<>();
		mappingBSE.put("exchange", "BSE");
		mappingBSE.put("code", "500114");
		Map<String, String> mappingNSE = new HashMap<>();
		mappingNSE.put("exchange", "NSE");
		mappingNSE.put("code", "500115");
		codes.add(mappingNSE);
		codes.add(mappingBSE);
		mappingBody.put("company_codes", codes);
		mappingRepository.addMapping("Flipkart", codes);
	}

	@AfterAll
	public void destroy() {
		mappingRepository.deleteAll();
		companyRepository.deleteAll();
		exchangeRepository.deleteAll();
		sectorRepository.deleteAll();
	}

	@Test
	public void testMapCompanyExchanges200() throws Exception {
		Map<String, Object> mappingBody = new HashMap<>();
		mappingBody.put("company_name", "Amazon");
		List<Map<String, String>> codes = new ArrayList<>();
		Map<String, String> mappingBSE = new HashMap<>();
		mappingBSE.put("exchange", "BSE");
		mappingBSE.put("code", "500112");
		Map<String, String> mappingNSE = new HashMap<>();
		mappingNSE.put("exchange", "NSE");
		mappingNSE.put("code", "500113");
		codes.add(mappingNSE);
		codes.add(mappingBSE);
		mappingBody.put("company_codes", codes);

		this.mockMvc
				.perform(post("/mapStockExchanges").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(mappingBody)))
				.andDo(print()).andExpect(status().isOk());

		List<StockExchangeEntity> mappedExchanges = mappingRepository.findStockExchangesForCompany("Amazon");
		assertEquals(mappedExchanges.size(), 2);
	}

	@Test
	public void testExchangesToCompany200() throws Exception {
		this.mockMvc.perform(get("/exchangesToCompany/Flipkart")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testExchangesToCompanyEmpty200() throws Exception {
		this.mockMvc.perform(get("/exchangesToCompany/HDFC")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void testDeactivateCompany200() throws Exception {
		this.mockMvc.perform(get("/deactivateCompany/Flipkart")).andDo(print()).andExpect(status().isOk());
	}
}
