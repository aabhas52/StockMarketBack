package com.project.stockmarket.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.StockExchangeEntity;
import com.project.stockmarket.entities.StockPriceEntity;

public interface StockPriceRepository extends JpaRepository<StockPriceEntity, Long>{

	public List<StockPriceEntity> findByCompany(CompanyEntity company);
	
	public List<StockPriceEntity> findByStockExchange(StockExchangeEntity exchange);
	
	public List<StockPriceEntity> findByCompanyAndDateBetween(CompanyEntity company, Date startDate, Date endDate);
	
	public StockPriceEntity findTop1ByCompanyOrderByDateDesc(CompanyEntity company);

	public List<StockPriceEntity> findByCompanyAndDateBetween(CompanyEntity company, LocalDate startDate,
			LocalDate endDate);
}
