package com.supermarket.simplechekout.domin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
    name = "sku_order_item"
)
public class SkuOrderItem {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sku_order_id", referencedColumnName = "id")
    @NotNull( message = "An item order must have a skuOrder associated")
	private SkuOrder skuOrder;

	@ManyToOne
	@JoinColumn(name = "sku_id", referencedColumnName = "id")
    @NotNull( message = "An order must have a sku associated")
	private Sku sku;

	@Column(name="tot_quantity")
	@NotNull( message = "An order must have a sku quantity associated")
	@DecimalMin(value="0.00")
	private Double quantity;

	public SkuOrderItem(){
		super();
	}
	
	public SkuOrderItem(SkuOrder skuOrder, Sku sku, Double quantity) {
		this();
		this.skuOrder = skuOrder;
		this.sku = sku;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SkuOrder getSkuOrder() {
		return skuOrder;
	}

	public void setSkuOrder(SkuOrder skuOrder) {
		this.skuOrder = skuOrder;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		result = prime * result + ((skuOrder == null) ? 0 : skuOrder.hashCode());
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
		if (!(obj instanceof SkuOrderItem)) {
			return false;
		}
		SkuOrderItem other = (SkuOrderItem) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (sku == null) {
			if (other.sku != null) {
				return false;
			}
		} else if (!sku.equals(other.sku)) {
			return false;
		}
		if (skuOrder == null) {
			if (other.skuOrder != null) {
				return false;
			}
		} else if (!skuOrder.equals(other.skuOrder)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SkuOrderItem [id=" + id + ", sku=" + sku + ", quantity=" + quantity + "]";
	}


}
