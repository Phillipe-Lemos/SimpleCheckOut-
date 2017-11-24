package com.supermarket.simplechekout.domin;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(
    name = "sku_order"
)
public class SkuOrder {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="created", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startDate;
	
	@Column(name="finished", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime finishedDate;
	
	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "id")
    @NotNull( message = "An order must have a client associated")
	private Client client;

	@Column(name="tot_quantity")
	private Double quantity;

	@Column(name="tot_discount")
	private Double totalDiscount;
	
	@Column(name="total_before_discount")
	private Double totalBeforeDiscount;

	@Column(name="amout_pay")
	private Double amoutPay;

	public SkuOrder() {
		super();
		// default value for each order.
		this.quantity = 0D;
		this.amoutPay = 0D;
		this.totalBeforeDiscount = 0D;
		this.totalDiscount = 0D;
		this.startDate = ZonedDateTime.now();
	}

	public SkuOrder(Client client) {
		this();
		this.client = client;
	}

	public SkuOrder(Long id, Client client, Double quantity, Double totalDiscount, Double amoutPay) {
		this();
		this.id = id;
		this.client = client; 
		this.quantity = quantity;
		this.totalDiscount = totalDiscount;
		this.amoutPay = amoutPay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getAmoutPay() {
		return amoutPay;
	}

	public void setAmoutPay(Double amoutPay) {
		this.amoutPay = amoutPay;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		if (!(obj instanceof SkuOrder)) {
			return false;
		}
		SkuOrder other = (SkuOrder) obj;
		if (client == null) {
			if (other.client != null) {
				return false;
			}
		} else if (!client.equals(other.client)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!startDate.equals(other.startDate)) {
			return false;
		}
		return true;
	}

	public ZonedDateTime getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(ZonedDateTime finishedDate) {
		this.finishedDate = finishedDate;
	}

	public Double getTotalBeforeDiscount() {
		return totalBeforeDiscount;
	}

	public void setTotalBeforeDiscount(Double totalBeforeDiscount) {
		this.totalBeforeDiscount = totalBeforeDiscount;
	}

	@Override
	public String toString() {
		return "SkuOrder [id=" + id + "]";
	}

	
}
