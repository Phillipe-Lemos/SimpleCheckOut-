package com.supermarket.simplechekout.domin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
    name = "sku_promotion"
)
public class Promotion {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	@GeneratedValue
	private Long Id;
	
	@OneToOne
	@JoinColumn(name = "sku_id", referencedColumnName = "id")
	private Sku sku;
	
	@Column(name="qtd_minimal")
	private Integer qtdMinimal;
	
	@Column(name="price")
	private Double price;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getQtdMinimal() {
		return qtdMinimal;
	}

	public void setQtdMinimal(Integer qtdMinimal) {
		this.qtdMinimal = qtdMinimal;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((qtdMinimal == null) ? 0 : qtdMinimal.hashCode());
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Promotion)) {
			return false;
		}
		Promotion other = (Promotion) obj;
		if (Id == null) {
			if (other.Id != null) {
				return false;
			}
		} else if (!Id.equals(other.Id)) {
			return false;
		}
		if (qtdMinimal == null) {
			if (other.qtdMinimal != null) {
				return false;
			}
		} else if (!qtdMinimal.equals(other.qtdMinimal)) {
			return false;
		}
		if (sku == null) {
			if (other.sku != null) {
				return false;
			}
		} else if (!sku.equals(other.sku)) {
			return false;
		}
		return true;
	}

}
