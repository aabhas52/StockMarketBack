package com.project.stockmarket.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.CompanyEntity;
import com.project.stockmarket.entities.Sector;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{
	
	public CompanyEntity findById(long id);
	
	public CompanyEntity findByCompanyName(String companyName);
	
	public List<CompanyEntity> findAllBySector(Sector sector);

	public List<CompanyEntity> findByCompanyNameContaining(String text);
}
