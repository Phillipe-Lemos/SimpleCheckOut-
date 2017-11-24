package com.supermarket.simplechekout.service;

import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.exception.ConstraintsViolationException;

public interface SkuOrderService {
	
	/**
	 * Check-out a specific item.   
	 * 
	 * @param idClient   Client identifier
	 * 
	 * @param idSku      Sku identifier
	 * 
	 * @param quantity   Sku quantity.
	 * 
	 * @throws IllegalArgumentException is thrown if one of the parameters was missing or are invalid. 
	 * @throws ConfictException if the some constraint were violated.      
	 */
	SkuOrderItem addOrderItem(Long idClient, Long idSku, Double quantity) throws IllegalArgumentException, 
	                                                                           ConstraintsViolationException;
	
	/**
	 * Summarizes the amount to pay considering the promotions.
	 * 
	 * @param idClient   Client identifier.
	 * 
	 * @return skuOrder with summarizes informations about the goods acquired by a client. 
	 * 
	 * @throws IllegalArgumentException if idClient was an invalid identifier.
	 *         ConstraintsViolationException if has same problems to save information.
	 */	
	SkuOrder totalOrderByClient(Long clientId) throws IllegalArgumentException,  ConstraintsViolationException;

}
