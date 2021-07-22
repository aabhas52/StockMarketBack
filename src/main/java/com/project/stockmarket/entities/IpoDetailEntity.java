package com.project.stockmarket.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "IpoDetails")
public class IpoDetailEntity {

	@Id
	@Column(name = "ipo_id")
	@GeneratedValue
	private long id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "company_id")
	private CompanyEntity company;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_exchange_code")
	private StockExchangeEntity stockExchange;
	
	@NotNull
	private double pricePerShare;
	
	@NotNull
	private long numberOfShares;
	
	@NotNull
	private LocalDate openingDate;
	
	@NotNull
	private LocalTime openingTime;
	
	private String remarks;
	
	protected IpoDetailEntity() {}

	public IpoDetailEntity(CompanyEntity company, StockExchangeEntity stockExchange, double pricePerShare,
			long numberOfShares, @NotNull LocalDate openingDate, @NotNull LocalTime openingTime, String remarks) {
		this.company = company;
		this.stockExchange = stockExchange;
		this.pricePerShare = pricePerShare;
		this.numberOfShares = numberOfShares;
		this.openingDate = openingDate;
		this.openingTime = openingTime;
		this.remarks = remarks;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	public StockExchangeEntity getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(StockExchangeEntity stockExchange) {
		this.stockExchange = stockExchange;
	}

	public double getPricePerShare() {
		return pricePerShare;
	}

	public void setPricePerShare(double pricePerShare) {
		this.pricePerShare = pricePerShare;
	}

	public long getNumberOfShares() {
		return numberOfShares;
	}

	public void setNumberOfShares(long numberOfShares) {
		this.numberOfShares = numberOfShares;
	}

	public LocalDate getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	public LocalTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(LocalTime openingTime) {
		this.openingTime = openingTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "IpoDetailEntity [id=" + id + ", company=" + company + ", stockExchange=" + stockExchange
				+ ", pricePerShare=" + pricePerShare + ", numberOfShares=" + numberOfShares + ", openingDate="
				+ openingDate + ", openingTime=" + openingTime + ", remarks=" + remarks + "]";
	}
	
	
}
