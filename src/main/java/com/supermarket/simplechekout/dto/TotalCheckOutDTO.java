package com.supermarket.simplechekout.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalCheckOutDTO {

	private String nameSku;
	
	private Double totalPrice;

	
	public Double getTotalPrice() {
		return totalPrice;
	}

	public TotalCheckOutDTO(String nameSku, Double totalPrice) {
		super();
		this.nameSku = nameSku;
		this.totalPrice = totalPrice;
	}

	public String getNameSku() {
		return nameSku;
	}


}
