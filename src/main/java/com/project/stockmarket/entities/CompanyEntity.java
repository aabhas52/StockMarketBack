package com.project.stockmarket.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "company")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CompanyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String companyName;
	
	@NotNull
	private double turnover;
	
	@NotNull
	private String ceo;
	
	private String writeup;
	
	@NotNull
	@Type(type = "text")
	private String directors;
	
//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.REMOVE)
//    @JsonIgnore
//    private IPODetail ipo;
	
	@OneToMany(targetEntity = CompanyAndStockExchanges.class)
	private List<CompanyAndStockExchanges> exchanges;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_id")
	private Sector sector;
		
	protected CompanyEntity() {};
	
	public CompanyEntity(String companyName, double turnover, String ceo, String writeup,
			Sector sector, String directors) {
		this.companyName = companyName;
		this.turnover = turnover;
		this.ceo = ceo;
		this.writeup = writeup;
		this.sector = sector;
		this.directors = directors;
	}

	public List<CompanyAndStockExchanges> getExchanges() {
		return exchanges;
	}

	public void setExchanges(List<CompanyAndStockExchanges> exchanges) {
		this.exchanges = exchanges;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public double getTurnover() {
		return turnover;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public long getId() {
		return id;
	}

	public String getWriteup() {
		return writeup;
	}

	public void setWriteup(String writeup) {
		this.writeup = writeup;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}
	
	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	@Override
	public String toString() {
		return "CompanyEntity [id=" + id + ", companyName=" + companyName + ", turnover=" + turnover + ", ceo=" + ceo
				+ ", writeup=" + writeup + ", directors=" + directors + ", sector=" + sector + "]";
	}
	
	
}
