package com.project.stockmarket.testClasses;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;
import com.project.stockmarket.repositories.CompanyRepository;
import com.project.stockmarket.repositories.SectorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class CompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	public void loadData() {
		sectorRepository.save(new Sector("Internet Service", "Provide online services to people"));
		sectorRepository.save(new Sector("Finance", "Provide banking solutions"));
	}

	@AfterEach
	public void destroyCompany() {
		companyRepository.deleteAll();
	}

	@AfterAll
	public void destroySector() {
		sectorRepository.deleteAll();
	}

	@Test
	public void testAddCompany404() throws Exception {

		String company = "{\"company_name\": \"Amazon\", \"ceo\": \"Andy Jassy\", \"directors\": \"none\", \"turnover\": 100, \"writeup\": \"Online shopping\", \"sector\": \"Cloud service\"}";

		ResultActions resultActions = this.mockMvc
				.perform(post("/addCompany").contentType(MediaType.APPLICATION_JSON).content(company)).andDo(print())
				.andExpect(status().isBadRequest());
		String result = resultActions.andReturn().getResponse().getContentAsString();
		assertEquals(result, "Sector not defined");
	}

	@Test
	public void testAddCompany200() throws Exception {

		String company = "{\"company_name\": \"Amazon\", \"ceo\": \"Andy Jassy\", \"directors\": \"none\", \"turnover\": 100, \"writeup\": \"Online shopping\", \"sector\": \"Internet Service\"}";

		ResultActions resultActions = this.mockMvc
				.perform(post("/addCompany").contentType(MediaType.APPLICATION_JSON).content(company)).andDo(print())
				.andExpect(status().isOk());
		String result = resultActions.andReturn().getResponse().getContentAsString();
		assertEquals(result, "Company Added");
	}

	@Test
	public void testEditCompany200() throws JsonProcessingException, Exception {

		Optional<Sector> sector = sectorRepository.findBySectorName("Internet Service");

		CompanyEntity company = new CompanyEntity("Amazon", 100, "Jeff Bezos", "", sector.get(), "Online shopping");
		companyRepository.save(company);

		company.setCeo("Andy Jassy");
		company.setTurnover(1000);
		company.setWriteup("Edited brief");

		ResultActions resultActions = this.mockMvc.perform(post("/editCompany").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(company))).andDo(print()).andExpect(status().isOk());

		String result = resultActions.andReturn().getResponse().getContentAsString();
		assertEquals(result, "Company Edited");
	}

	@Test
	public void testAllCompanies200() throws Exception {

		Optional<Sector> sector = sectorRepository.findBySectorName("Internet Service");

		CompanyEntity company = new CompanyEntity("Amazon", 100, "Jeff Bezos", "Online Shopping", sector.get(),
				"Andy Jassy");
		companyRepository.save(company);

		this.mockMvc.perform(get("/allCompanies")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].companyName").value("Amazon")).andExpect(jsonPath("$[0].turnover").value(100))
				.andExpect(jsonPath("$[0].ceo").value("Jeff Bezos"))
				.andExpect(jsonPath("$[0].directors").value("Andy Jassy"))
				.andExpect(jsonPath("$[0].sector.sectorName").value("Internet Service"))
				.andExpect(jsonPath("$[0].writeup").value("Online Shopping"));

	}

	@Test
	public void testAllCompaniesSector200() throws Exception {
		Optional<Sector> sector1 = sectorRepository.findBySectorName("Internet Service");
		Optional<Sector> sector2 = sectorRepository.findBySectorName("Finance");

		CompanyEntity company1 = new CompanyEntity("Amazon", 100, "Jeff Bezos", "Online Shopping", sector1.get(),
				"Andy Jassy");
		companyRepository.save(company1);
		CompanyEntity company2 = new CompanyEntity("Flipkart", 200, "Sachin Bansal", "Online Shopping for India",
				sector2.get(), "Kalyan Krishnamurthy");
		companyRepository.save(company2);

		this.mockMvc.perform(get("/allCompanies/{sectorName}", sector2.get().getSectorName())).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].companyName").value("Flipkart"))
				.andExpect(jsonPath("$[0].turnover").value(200)).andExpect(jsonPath("$[0].ceo").value("Sachin Bansal"))
				.andExpect(jsonPath("$[0].directors").value("Kalyan Krishnamurthy"))
				.andExpect(jsonPath("$[0].sector.sectorName").value("Finance"))
				.andExpect(jsonPath("$[0].writeup").value("Online Shopping for India"));
	}

	@Test
	public void testFindMatchingCompanyEmpty200() throws Exception {

		this.mockMvc.perform(get("/findMatchingCompany/Am")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void testFindMatchingCompany200() throws Exception {

		Optional<Sector> sector = sectorRepository.findBySectorName("Internet Service");

		CompanyEntity company = new CompanyEntity("Amazon", 100, "Jeff Bezos", "Online Shopping", sector.get(),
				"Andy Jassy");
		companyRepository.save(company);

		this.mockMvc.perform(get("/findMatchingCompany/am")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].companyName").value("Amazon"));
	}
}
