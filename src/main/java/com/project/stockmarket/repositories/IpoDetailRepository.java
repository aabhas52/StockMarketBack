package com.project.stockmarket.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.IpoDetailEntity;
import com.project.stockmarket.entities.StockExchangeEntity;

public interface IpoDetailRepository extends JpaRepository<IpoDetailEntity, Long>{
	
	public IpoDetailEntity findById(long id);

	public List<IpoDetailEntity> findByCompanyOrderByOpeningDateDesc(CompanyEntity company);

	public List<IpoDetailEntity> findAllByOrderByOpeningDateDesc();
	
	public Optional<IpoDetailEntity> findByCompanyAndStockExchange(CompanyEntity company, StockExchangeEntity stockExchangeEntity);
}
