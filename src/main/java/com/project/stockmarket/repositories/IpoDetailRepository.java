package com.project.stockmarket.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.IpoDetailEntity;

public interface IpoDetailRepository extends JpaRepository<IpoDetailEntity, Long>{
	
	public IpoDetailEntity findById(long id);

	public List<IpoDetailEntity> findByCompanyOrderByOpeningDateDesc(CompanyEntity company);

	public List<IpoDetailEntity> findAllByOrderByOpeningDateDesc();
}
