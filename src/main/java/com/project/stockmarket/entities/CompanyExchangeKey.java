package com.project.stockmarket.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompanyExchangeKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6535199731883352487L;

	private long companyId;
	
	private String stockExchangeCode;
	
	protected CompanyExchangeKey() {}
	
	public CompanyExchangeKey(long companyId, String stockExchangeCode) {
		super();
		this.companyId = companyId;
		this.stockExchangeCode = stockExchangeCode;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getStockExchangeCode() {
		return stockExchangeCode;
	}

	public void setStockExchangeCode(String stockExchangeCode) {
		this.stockExchangeCode = stockExchangeCode;
	}
}
