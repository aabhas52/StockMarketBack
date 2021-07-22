package com.project.stockmarket.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.stockmarket.entities.CompanyAndStockExchanges;
import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.CompanyExchangeKey;
import com.project.stockmarket.entities.StockExchangeEntity;

@Repository
@Transactional
public class CompanyStockExchangesRepository {

	@Autowired
	EntityManager em;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	StockExchangeRepository stockExchangeRepository;
	
	public void addMapping(String companyName, List<Map<String, String>> companyCodes) {
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		for (Map<String, String> mapping : companyCodes) {
			String exchangeCode = mapping.get("exchange");
			String companyCode = mapping.get("code");
			Optional<StockExchangeEntity> exchange = stockExchangeRepository.findById(exchangeCode);
			CompanyExchangeKey key = new CompanyExchangeKey(company.getId(), exchangeCode);
			CompanyAndStockExchanges newMapping = new CompanyAndStockExchanges(key, company, exchange.get(), companyCode, true);
			em.merge(newMapping);
		}
	}
	
	public List<StockExchangeEntity> findStockExchangesForCompany(String companyName) {
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		List<CompanyAndStockExchanges> mapList = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE COMPANY_ID = :id AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("id", company.getId()).getResultList();
		List<StockExchangeEntity> exchangeList = new ArrayList<StockExchangeEntity>();
		mapList.forEach((mapping) -> {
			exchangeList.add(mapping.getStockExchangeEntity());
		});
		return exchangeList;
	}
	
	public List<StockExchangeEntity> findStockExchangesForCompanyById(long companyId) {
		List<CompanyAndStockExchanges> mapList = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE COMPANY_ID = :id AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("id", companyId).getResultList();
		List<StockExchangeEntity> exchangeList = new ArrayList<StockExchangeEntity>();
		mapList.forEach((mapping) -> {
			exchangeList.add(mapping.getStockExchangeEntity());
		});
		return exchangeList;
	}
	
	public List<CompanyEntity> findCompaniesForExchange(String exchangeCode){
		List<CompanyAndStockExchanges> mapList = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE STOCK_EXCHANGE_CODE = :exchangeCode AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("exchangeCode", exchangeCode).getResultList();
		List<CompanyEntity> companyList = new ArrayList<CompanyEntity>();
		mapList.forEach((mapping) -> {
			companyList.add(mapping.getCompanyEntity());
		});
		return companyList;
	}
	
	public List<CompanyAndStockExchanges> findByCompanyCode(String companyCode) {
		List<CompanyAndStockExchanges> result = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE COMPANY_CODE = :companyCode AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("companyCode", companyCode).getResultList();
		return result;
	}
	
	public List<CompanyAndStockExchanges> findByCompanyCodeAndExchange(String companyCode, String exchangeCode) {
		List<CompanyAndStockExchanges> result = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE COMPANY_CODE = :companyCode AND STOCK_EXCHANGE_CODE = :exchangeCode AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("companyCode", companyCode).
				setParameter("exchangeCode", exchangeCode).getResultList();
		return result;
	}
	
	public List<CompanyAndStockExchanges> findByCompanyIdAndExchange(long companyId, String exchangeCode) {
		List<CompanyAndStockExchanges> result = em.createNativeQuery(
				"SELECT * FROM COMPANY_AND_STOCK_EXCHANGES WHERE COMPANY_ID = :companyId AND STOCK_EXCHANGE_CODE = :exchangeCode AND ACTIVE = TRUE",
				CompanyAndStockExchanges.class).setParameter("companyId", companyId).
				setParameter("exchangeCode", exchangeCode).getResultList();
		return result;
	}
	
	public void deactivateCompany(String companyName) {
		CompanyEntity company = companyRepository.findByCompanyName(companyName);
		em.createNativeQuery("UPDATE COMPANY_AND_STOCK_EXCHANGES SET ACTIVE = FALSE WHERE COMPANY_ID = :id")
			.setParameter("id", company.getId());
	}
}
