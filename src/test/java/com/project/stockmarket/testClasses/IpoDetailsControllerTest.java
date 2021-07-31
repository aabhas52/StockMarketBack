package com.project.stockmarket.testClasses;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.IpoDetailEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.IpoDetailRepository;
import com.project.stockmarket.repositories.SectorRepository;
import com.project.stockmarket.repositories.StockExchangeRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class IpoDetailsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private IpoDetailRepository ipoRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private StockExchangeRepository exchangeRepository;

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

		IpoDetailEntity ipo1 = new IpoDetailEntity(company1, exchange, 350.15, 1000, LocalDate.now(), LocalTime.now(),
				"");
		IpoDetailEntity ipo2 = new IpoDetailEntity(company2, exchange, 490.26, 5000, LocalDate.now().minusDays(1L),
				LocalTime.now(), "");
		ipoRepository.save(ipo1);
		ipoRepository.save(ipo2);
	}

	@AfterAll
	public void destroy() {
		ipoRepository.deleteAll();
		companyRepository.deleteAll();
		sectorRepository.deleteAll();
		exchangeRepository.deleteAll();
	}

	@Test
	public void testListIpoByCompany200() throws Exception {
		this.mockMvc.perform(get("/companyIpo/Amazon")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].company.companyName").value("Amazon"));

	}

	@Test
	public void testListIpoByCompanyNotPresent200() throws Exception {
		this.mockMvc.perform(get("/companyIpo/HDFC")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

	}

	@Test
	public void testEditPresentIpo200() throws JsonProcessingException, Exception {

		CompanyEntity company = companyRepository.findByCompanyName("Amazon");
		StockExchangeEntity exchange = exchangeRepository.findAll().get(0);

		String editedIpo = "{\"company_name\":\"Amazon\",\"stock_exchange_code\":\"BSE\",\"price\":945.23,\"shares\":1200,\"date\":\"2021-07-31\",\"time\":\"17:37:50\",\"remarks\":\"\"}";

		this.mockMvc.perform(post("/editIpo").contentType(MediaType.APPLICATION_JSON).content(editedIpo)).andDo(print())
				.andExpect(status().isOk());

		IpoDetailEntity ipo = ipoRepository.findByCompanyAndStockExchange(company, exchange).get();
		assertEquals(ipo.getCompany().getCompanyName(), "Amazon");
		assertEquals(ipo.getStockExchange().getStockExchangeCode(), "BSE");
		assertEquals(ipo.getNumberOfShares(), 1200);
		assertEquals(ipo.getPricePerShare(), 945.23);
	}

	@Test
	public void testEditIpoNew200() throws JsonProcessingException, Exception {

		CompanyEntity company = companyRepository.findByCompanyName("HDFC");
		StockExchangeEntity exchange = exchangeRepository.findAll().get(0);

		String newIpo = "{\"company_name\":\"HDFC\",\"stock_exchange_code\":\"BSE\",\"price\":1562.24,\"shares\":1500,\"date\":\"2021-06-30\",\"time\":\"17:37:50\",\"remarks\":\"\"}";

		this.mockMvc.perform(post("/editIpo").contentType(MediaType.APPLICATION_JSON).content(newIpo)).andDo(print())
				.andExpect(status().isOk());

		IpoDetailEntity ipo = ipoRepository.findByCompanyAndStockExchange(company, exchange).get();
		assertEquals(ipo.getCompany().getCompanyName(), "HDFC");
		assertEquals(ipo.getStockExchange().getStockExchangeCode(), "BSE");
		assertEquals(ipo.getNumberOfShares(), 1500);
		assertEquals(ipo.getPricePerShare(), 1562.24);
	}

	@Test
	public void testAllIpos200() throws Exception {
		this.mockMvc.perform(get("/allIPOs")).andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].company.companyName").value("Amazon"))
				.andExpect(jsonPath("$[1].company.companyName").value("Flipkart"));
	}
}
