package com.supermarket.simplechekout.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.dto.CheckoutTotalsDTO;
import com.supermarket.simplechekout.dto.PurchaseSkuDTO;
import com.supermarket.simplechekout.dto.TotalCheckOutDTO;
import com.supermarket.simplechekout.exception.BadRequestException;
import com.supermarket.simplechekout.exception.ConstraintsViolationException;
import com.supermarket.simplechekout.service.SkuOrderService;

@RestController
@RequestMapping("rest/order")
public class OrderController {

	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
	
	private SkuOrderService skuOrderService;
	
	@Autowired
	public OrderController(SkuOrderService skuOrderService){
		this.skuOrderService = skuOrderService;
	}
	
		
	/**
	 * Check out an item, adding it to a client purchase.
	 * 
	 * @param item  Item that was checked out, that is composed by the sku identifier, client identifier and sku quantity.   
	 * 
	 * @return The total of an item that is an instance of TotalSkuDTO. 
	 */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
	public TotalCheckOutDTO checkOutItem(@Valid @RequestBody PurchaseSkuDTO item) throws ConstraintsViolationException, BadRequestException{
    	LOG.info(">>> purchaseItem " + item);
    	SkuOrderItem added =  null;
    	try{
    		added =  skuOrderService.addOrderItem(item.getClientId(), item.getIdSku(), item.getQtdSku());
    	} catch(IllegalArgumentException illegalArgumentException){
    		throw new BadRequestException(illegalArgumentException.getMessage());
    	}
    	LOG.info(">>> after addOrderItem " + item);
    	return new TotalCheckOutDTO(added.getSku().getSkuName(), added.getSku().getPrice());
	}
    
    /**
     * 
     * @param clientId   Client identifier
     * @return           The totals of items ordered by a client identified by clientId         
     * @throws ConstraintsViolationException  throws if any constraint were violated.
     * @throws BadRequestException            throws if invalid client identifier was informed.
     */
    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutTotalsDTO getTotalcheckOutByClient(@Valid @PathVariable long clientId)  throws ConstraintsViolationException, BadRequestException{
    	LOG.info(">>> getTotalcheckOutByClient ");
    	SkuOrder  skuOrder = null;
    	try {
	    	skuOrder = skuOrderService.totalOrderByClient(clientId);

    	} catch(IllegalArgumentException illegalArgumentException){
    		throw new BadRequestException(illegalArgumentException.getMessage());
    	}
    	return new CheckoutTotalsDTO(new Date(), 
                skuOrder.getQuantity(), 
                skuOrder.getTotalBeforeDiscount(),
                skuOrder.getTotalDiscount(),
                skuOrder.getAmoutPay());
    }
	
}
