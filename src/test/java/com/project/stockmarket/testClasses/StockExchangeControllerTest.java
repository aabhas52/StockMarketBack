package com.project.stockmarket.testClasses;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.repositories.StockExchangeRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class StockExchangeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StockExchangeRepository exchangeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void loadData() {
		StockExchangeEntity exchange1 = new StockExchangeEntity("BSE", "Bombay Stock Exchange",
				"Asia's oldest stock exchange", "Dalal Street, Mumbai", "");
		exchangeRepository.save(exchange1);
		StockExchangeEntity exchange2 = new StockExchangeEntity("NSE", "National Stock Exchange",
				"Leading stock exchange of India", "Bandra, Mumbai", "");
		exchangeRepository.save(exchange2);
	}

	@AfterAll
	public void destroy() {
		exchangeRepository.deleteAll();
	}

	@Test
	public void testAllStockExchanges200() throws Exception {

		this.mockMvc.perform(get("/allStockExchanges")).andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].stockExchangeCode").value("BSE"))
				.andExpect(jsonPath("$[1].stockExchangeCode").value("NSE"));
	}

	@Test
	public void testAddStockExchange200() throws Exception {
		StockExchangeEntity exchange = new StockExchangeEntity("NYSE", "New York Stock Exchange",
				"Leading stock exchange of the world", "New York", "");

		this.mockMvc.perform(post("/addStockExchange").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(exchange))).andDo(print()).andExpect(status().isOk());
	}
}
