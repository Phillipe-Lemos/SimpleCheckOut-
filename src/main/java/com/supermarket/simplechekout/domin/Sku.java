package com.supermarket.simplechekout.domin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
    name = "sku",
    uniqueConstraints = @UniqueConstraint(name = "uc_sku_name", columnNames = {"sku_name"})
)
public class Sku {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	@GeneratedValue
	private Long Id;
	
	@Column(name="sku_name")
	private String skuName;
	
	@Column(name="sku_actual_price")
	private Double price;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Sku(){
		super();
	}
	
	public Sku(Long id, String skuName, Double price) {
		this();
		Id = id;
		this.skuName = skuName;
		this.price = price;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((skuName == null) ? 0 : skuName.hashCode());
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
		if (!(obj instanceof Sku)) {
			return false;
		}
		Sku other = (Sku) obj;
		if (Id == null) {
			if (other.Id != null) {
				return false;
			}
		} else if (!Id.equals(other.Id)) {
			return false;
		}
		if (price == null) {
			if (other.price != null) {
				return false;
			}
		} else if (!price.equals(other.price)) {
			return false;
		}
		if (skuName == null) {
			if (other.skuName != null) {
				return false;
			}
		} else if (!skuName.equals(other.skuName)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Sku [Id=" + Id + ", skuName=" + skuName + ", price=" + price + "]";
	}
	
	
}
