package com.project.stockmarket.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Sector {

	@Id
	@GeneratedValue
	@Column(name = "sector_id")
	private long id;
	
	@NotNull
	private String sectorName;
	
	private String brief;

	@OneToMany(mappedBy = "sector")
	@JsonIgnore
	private List<CompanyEntity> companies;
	
	protected Sector() {}

	public Sector(String sectorName, String brief) {
		this.sectorName = sectorName;
		this.brief = brief;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public long getId() {
		return id;
	}

	public List<CompanyEntity> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyEntity> companies) {
		this.companies = companies;
	}

	@Override
	public String toString() {
		return "Sector [id=" + id + ", sectorName=" + sectorName + ", brief=" + brief + ", companies=" + companies
				+ "]";
	}
}
