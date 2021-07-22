package com.project.stockmarket.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

@Entity
public class CompanyAndStockExchanges {

	@EmbeddedId
	private CompanyExchangeKey id;
	
	@ManyToOne
	@MapsId("companyId")
	@JoinColumn(name = "company_id")
	private CompanyEntity companyEntity;
	
	@ManyToOne
	@MapsId("stockExchangeCode")
	@JoinColumn(name = "stock_exchange_code")
	private StockExchangeEntity stockExchangeEntity;
	
	@NotNull
	private String companyCode;
	
	private boolean active;
	
	protected CompanyAndStockExchanges() {}

	public CompanyAndStockExchanges(CompanyExchangeKey id, CompanyEntity companyEntity,
			StockExchangeEntity stockExchangeEntity, String companyCode, boolean active) {
		this.id = id;
		this.companyEntity = companyEntity;
		this.stockExchangeEntity = stockExchangeEntity;
		this.companyCode = companyCode;
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public CompanyExchangeKey getId() {
		return id;
	}

	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public StockExchangeEntity getStockExchangeEntity() {
		return stockExchangeEntity;
	}

	@Override
	public String toString() {
		return "CompanyAndStockExchanges [id=" + id + ", companyEntity=" + companyEntity + ", stockExchangeEntity="
				+ stockExchangeEntity + ", companyCode=" + companyCode + ", active=" + active + "]";
	}
}
