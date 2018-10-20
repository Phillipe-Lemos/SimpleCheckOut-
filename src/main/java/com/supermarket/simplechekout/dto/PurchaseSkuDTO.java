package com.supermarket.simplechekout.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseSkuDTO {
	
 
	@NotNull(message = "The sku must have a valid identifier.")
	@Min(value=1)
	@JsonProperty("id_sku")
	private Long idSku;

	@NotNull(message = "The purchase must have a quantity.")
	@DecimalMin(value="0.00")
	@JsonProperty("qtd_sku")
	private Double qtdSku;
	
	@NotNull(message = "The client must have a valid identifier.")
	@Min(value=1)
	@JsonProperty("id_client")
	private Long clientId;
	
	public Long getIdSku() {
		return idSku;
	}

	public Double getQtdSku() {
		return qtdSku;
	}

	public Long getClientId() {
		return clientId;
	}
	
	private PurchaseSkuDTO(){
		super();
	}
	
	public PurchaseSkuDTO(Long idSku, Double qtdSku, Long clientId) {
		this();
		this.idSku = idSku;
		this.qtdSku = qtdSku;
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "PurchaseSkuDTO [idSku=" + idSku + ", qtdSku=" + qtdSku + ", clientId=" + clientId + "]";
	}

}
