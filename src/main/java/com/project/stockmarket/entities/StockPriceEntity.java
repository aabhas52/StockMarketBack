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
@Table(name = "stock_prices")
public class StockPriceEntity {
	
	@Id
	@Column(name = "stock_price_id")
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
	private double currentPrice;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private LocalTime time;
	
	protected StockPriceEntity() {};
	
	public StockPriceEntity(CompanyEntity company, StockExchangeEntity stockExchange, double currentPrice, LocalDate date,
			LocalTime time) {
		this.company = company;
		this.stockExchange = stockExchange;
		this.currentPrice = currentPrice;
		this.date = date;
		this.time = time;
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

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "StockPriceEntity [id=" + id + ", company=" + company + ", stockExchange=" + stockExchange
				+ ", currentPrice=" + currentPrice + ", date=" + date + ", time=" + time + "]";
	}
}
