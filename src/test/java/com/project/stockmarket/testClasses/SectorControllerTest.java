package com.project.stockmarket.testClasses;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.repositories.SectorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class SectorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SectorRepository sectorRepository;

	@BeforeAll
	public void loadData() {
		sectorRepository.save(new Sector("Internet Service", "Provide online services to people"));
		sectorRepository.save(new Sector("Finance", "Provide banking solutions"));
	}

	@AfterAll
	public void destroy() {
		sectorRepository.deleteAll();
	}

	@Test
	public void testAllSectors200() throws Exception {

		this.mockMvc.perform(get("/allSectors")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].sectorName").value("Internet Service"))
				.andExpect(jsonPath("$[1].sectorName").value("Finance"));
	}

}
