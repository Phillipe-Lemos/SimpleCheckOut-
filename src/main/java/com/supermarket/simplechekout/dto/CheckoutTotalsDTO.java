package com.supermarket.simplechekout.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutTotalsDTO {
	
	private Date checkOutDate;
	
	private Double totalNumberItens;
	
	private Double totalValue;
	
	private Double totalDiscount;
	
	private Double totalAmoutPay;

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public Double getTotalNumberItens() {
		return totalNumberItens;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public Double getTotalAmoutPay() {
		return totalAmoutPay;
	}

	private CheckoutTotalsDTO(){
		super();
	}
	
	public CheckoutTotalsDTO(Date checkOutDate, Double totalNumberItens, Double totalValue, Double totalDiscount,
			Double totalAmoutPay) {
		this();
		this.checkOutDate = checkOutDate;
		this.totalNumberItens = totalNumberItens;
		this.totalValue = totalValue;
		this.totalDiscount = totalDiscount;
		this.totalAmoutPay = totalAmoutPay;
	}
}
