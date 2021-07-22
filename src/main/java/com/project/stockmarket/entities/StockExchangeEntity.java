package com.project.stockmarket.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stock_exchange")
public class StockExchangeEntity {
	
	@Id
	private String stockExchangeCode;
	
	@NotNull
	private String stockExchangeName;
	
	private String brief;
	
	@NotNull
	private String contactAddress;
	
	private String remarks;
	
	@OneToMany(targetEntity = CompanyAndStockExchanges.class)
	private List<CompanyAndStockExchanges> companies;
	
	protected StockExchangeEntity() {}

	public StockExchangeEntity(String stockExchangeCode, String stockExchangeName, String brief, String contactAddress,
			String remarks) {
		super();
		this.stockExchangeCode = stockExchangeCode;
		this.stockExchangeName = stockExchangeName;
		this.brief = brief;
		this.contactAddress = contactAddress;
		this.remarks = remarks;
	}

	public List<CompanyAndStockExchanges> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyAndStockExchanges> companies) {
		this.companies = companies;
	}

	public String getStockExchangeCode() {
		return stockExchangeCode;
	}

	public void setStockExchangeCode(String stockExchangeCode) {
		this.stockExchangeCode = stockExchangeCode;
	}

	public String getStockExchangeName() {
		return stockExchangeName;
	}

	public void setStockExchangeName(String stockExchangeName) {
		this.stockExchangeName = stockExchangeName;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "StockExchangeEntity [stockExchangeCode=" + stockExchangeCode + ", stockExchangeName="
				+ stockExchangeName + ", brief=" + brief + ", contactAddress=" + contactAddress + ", remarks=" + remarks
				+ ", companies=" + companies + "]";
	}
	
}
