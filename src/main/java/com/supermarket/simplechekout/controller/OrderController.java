package com.supermarket.simplechekout.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.dto.CheckoutTotalsDTO;
import com.supermarket.simplechekout.dto.PurchaseSkuDTO;
import com.supermarket.simplechekout.dto.TotalCheckOutDTO;
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
	public ResponseEntity<TotalCheckOutDTO> checkOutItem(@Valid @RequestBody PurchaseSkuDTO item) {
    	LOG.info(">>> purchaseItem " + item);
    	SkuOrderItem added =  skuOrderService.addOrderItem(item.getClientId(), item.getIdSku(), item.getQtdSku());
    	LOG.info(">>> after addOrderItem " + item);
    	final ResponseEntity<TotalCheckOutDTO> responseEntity 
    	                 = new ResponseEntity<>(new TotalCheckOutDTO(added.getSku().getSkuName(), 
                                                                  added.getSku().getPrice()), 
    			                                           HttpStatus.CREATED); 
    	return responseEntity;
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
    public ResponseEntity<CheckoutTotalsDTO> getTotalcheckOutByClient(@Valid @PathVariable long clientId) {
    	LOG.info(">>> getTotalcheckOutByClient ");
    	final SkuOrder  skuOrder = skuOrderService.totalOrderByClient(clientId);
    	return ResponseEntity.ok(new CheckoutTotalsDTO(new Date(), 
                skuOrder.getQuantity(), 
                skuOrder.getTotalBeforeDiscount(),
                skuOrder.getTotalDiscount(),
                skuOrder.getAmoutPay()));
    }
    
    @ExceptionHandler({IllegalArgumentException.class})
	private ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException illegal) {
    	LOG.error(illegal.getMessage(), illegal);
		return new ResponseEntity<String>(illegal.getMessage(), HttpStatus.BAD_REQUEST);
	}
		
    @ExceptionHandler({JsonParseException.class, 
    	               JsonMappingException.class, 
    	               InvalidFormatException.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    private void handleJsonException(JsonParseException ex) {
    	LOG.error("Json problems", ex);
    }
    
    @ExceptionHandler({MismatchedInputException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private void handleJsonFormatException(MismatchedInputException ex) {
    	LOG.error("Problems with inputstreams :", ex);
    }
		
}
