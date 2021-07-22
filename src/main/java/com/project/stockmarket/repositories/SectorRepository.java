package com.project.stockmarket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.Sector;

public interface SectorRepository extends JpaRepository<Sector, Long>{

	public Optional<Sector> findBySectorName(String name);
	
}
