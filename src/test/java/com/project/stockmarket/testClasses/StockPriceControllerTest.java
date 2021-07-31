package com.project.stockmarket.testClasses;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.entities.StockPriceEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.CompanyStockExchangesRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;
import com.project.stockmarket.repositories.StockPriceRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class StockPriceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StockExchangeRepository exchangeRepository;

	@Autowired
	private CompanyStockExchangesRepository mappingRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private StockPriceRepository priceRepostiory;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void loadData() {
		Sector sector1 = new Sector("Internet Service", "Provide online services to people");
		sectorRepository.save(sector1);
		Sector sector2 = new Sector("Finance", "Provide banking solutions");
		sectorRepository.save(sector2);

		CompanyEntity company1 = new CompanyEntity("Amazon", 100, "Jeff Bezos", "Online Shopping", sector1,
				"Andy Jassy");
		companyRepository.save(company1);
		CompanyEntity company2 = new CompanyEntity("Flipkart", 200, "Sachin Bansal", "Online Shopping for India",
				sector1, "Kalyan Krishnamurthy");
		companyRepository.save(company2);
		CompanyEntity company3 = new CompanyEntity("HDFC", 300, "Sashidhar", "Banking solutions", sector2, " None");
		companyRepository.save(company3);

		StockExchangeEntity exchange = new StockExchangeEntity("BSE", "Bombay Stock Exchange",
				"Asia's oldest stock exchange", "Dalal Street, Mumbai", "");
		exchangeRepository.save(exchange);

		Map<String, String> mapping = new HashMap<>();
		mapping.put("exchange", "BSE");
		mapping.put("code", "500112");
		List<Map<String, String>> mappingList = new ArrayList<>();
		mappingList.add(mapping);
		mappingRepository.addMapping("Amazon", mappingList);

		StockPriceEntity stock1 = new StockPriceEntity(company3, exchange, 1250.26, LocalDate.of(2021, 07, 30),
				LocalTime.now());
		StockPriceEntity stock2 = new StockPriceEntity(company3, exchange, 1246.78, LocalDate.of(2021, 07, 28),
				LocalTime.now());
		StockPriceEntity stock3 = new StockPriceEntity(company3, exchange, 1203.16, LocalDate.of(2021, 07, 28),
				LocalTime.now().minusHours(3L));
		StockPriceEntity stock4 = new StockPriceEntity(company2, exchange, 498.52, LocalDate.of(2021, 07, 31),
				LocalTime.now());
		priceRepostiory.save(stock1);
		priceRepostiory.save(stock2);
		priceRepostiory.save(stock3);
		priceRepostiory.save(stock4);
	}

	@AfterAll
	public void destroy() {
		priceRepostiory.deleteAll();
		mappingRepository.deleteAll();
		companyRepository.deleteAll();
		exchangeRepository.deleteAll();
		sectorRepository.deleteAll();
	}

	@Test
	public void testAddStockPriceByCodeEmptyList400() throws Exception {
		List<Map<String, Object>> emptyList = new ArrayList<>();
		this.mockMvc
				.perform(post("/addStockPriceByCode").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emptyList)))
				.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("No records"));
	}

	@Test
	public void testAddStockPriceByCodeNoMapping400() throws Exception {
		List<Map<String, Object>> priceList = new ArrayList<>();
		Map<String, Object> mapping = new HashMap<>();
		mapping.put("company_code", "500114");
		mapping.put("stock_exchange_code", "BSE");
		priceList.add(mapping);

		this.mockMvc
				.perform(post("/addStockPriceByCode").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(priceList)))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Company code not found in stock exchange"));
	}

	@Test
	public void testAddStockPriceByCode200() throws Exception {
		List<Map<String, Object>> priceList = new ArrayList<>();
		Map<String, Object> mapping = new HashMap<>();
		mapping.put("company_code", "500112");
		mapping.put("stock_exchange_code", "BSE");
		mapping.put("price", 350.15);
		mapping.put("date", "7/31/21");
		mapping.put("time", "19:05:26");
		priceList.add(mapping);

		this.mockMvc
				.perform(post("/addStockPriceByCode").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(priceList)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("1")).andExpect(jsonPath("$.company").value("Amazon"))
				.andExpect(jsonPath("$.exchange").value("Bombay Stock Exchange"));
	}

	@Test
	public void testFindPricesBetweenDates200() throws Exception {
		String request = "{\"company_name\": \"HDFC\", \"start\": \"2021-07-27\", \"end\": \"2021-07-31\"}";

		this.mockMvc.perform(post("/findPricesBetweenDates").contentType(MediaType.APPLICATION_JSON).content(request))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.2021-07-28").value(1224.97)).andExpect(jsonPath("$.2021-07-30").value(1250.26));
	}

	@Test
	public void testFindLatestPrice200() throws Exception {
		this.mockMvc.perform(get("/findLatestPrice/HDFC")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.company.companyName").value("HDFC"))
				.andExpect(jsonPath("$.currentPrice").value(1250.26)).andExpect(jsonPath("$.date").value("2021-07-30"));
	}

	@Test
	public void testFindLatestPrice204() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(get("/findLatestPrice/Amazon")).andDo(print())
				.andExpect(status().isNoContent());
		String result = resultActions.andReturn().getResponse().getContentAsString();
		assertEquals(result, "Not available");
	}

	@Test
	public void testSectorPrices400() throws Exception {
		String request = "{\"sector\": \"Healthcare\", \"start\": \"2021-07-27\", \"end\": \"2021-07-31\"}";

		this.mockMvc.perform(post("/sectorPrices").contentType(MediaType.APPLICATION_JSON).content(request))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void testSectorPrices200() throws Exception {
		String request = "{\"sector\": \"Internet Service\", \"start\": \"2021-07-27\", \"end\": \"2021-08-01\"}";

		this.mockMvc.perform(post("/sectorPrices").contentType(MediaType.APPLICATION_JSON).content(request))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.2021-07-31").value(498.52));

	}
}
