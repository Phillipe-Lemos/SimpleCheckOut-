package com.supermarket.simplechekout.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermarket.simplechekout.domin.Client;
import com.supermarket.simplechekout.domin.Promotion;
import com.supermarket.simplechekout.domin.Sku;
import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.exception.ConstraintsViolationException;
import com.supermarket.simplechekout.repository.ClientRepository;
import com.supermarket.simplechekout.repository.PromotionRepository;
import com.supermarket.simplechekout.repository.SkuOrderItemRepository;
import com.supermarket.simplechekout.repository.SkuOrderRepository;
import com.supermarket.simplechekout.repository.SkuRepository;
import com.supermarket.simplechekout.service.SkuOrderService;


@Service
public class SkuOrderServiceImpl implements SkuOrderService {

	private static final Logger LOG = LoggerFactory.getLogger(SkuOrderServiceImpl.class);
	
	private final SkuRepository skuRepository;
	
	private final SkuOrderRepository skuOrderRepository;
	
	private final SkuOrderItemRepository skuOrderItemRepository;
	
	private final ClientRepository clientRepository;
	
	private final PromotionRepository promotionRepository;
	
	@Autowired
	public SkuOrderServiceImpl(SkuRepository skuRepository, 
			                   SkuOrderRepository skuOrderRepository, 
			                   ClientRepository clientRepository,
			                   SkuOrderItemRepository skuOrderItemRepository,
			                   PromotionRepository promotionRepository){
		super();
		this.skuRepository = skuRepository;
		this.skuOrderRepository = skuOrderRepository;
		this.clientRepository = clientRepository;
		this.skuOrderItemRepository = skuOrderItemRepository;
		this.promotionRepository = promotionRepository;
	}
	
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
	@Transactional(rollbackFor = Exception.class)
	public SkuOrderItem addOrderItem(Long idClient, Long idSku, Double quantity) throws IllegalArgumentException, 
	                                                                                  ConstraintsViolationException{
		LOG.info(">>> addOrderItem " + idClient + " " + idSku + " " + quantity);
		if(idClient == null || idClient <= 0){
			throw new IllegalArgumentException("Invalid client identifier !");
		}
		Client client = clientRepository.findOne(idClient);
		
		if(client == null){
			throw new IllegalArgumentException("Invalid client identifier !");
		}
		
		
		if(idSku == null || idClient <= 0){
			throw new IllegalArgumentException("Invalid sku identifier !");
		}
		
		Sku sku = skuRepository.findOne(idSku);
		
		if(sku == null){
			throw new IllegalArgumentException("Invalid sku identifier !");
		}
		
		if(quantity <= 0){
			throw new IllegalArgumentException("Invalid quantity !");
		}
		SkuOrder skuOrder = null;
		SkuOrderItem skuOrderItem = null;
		try{
			skuOrder = skuOrderRepository.findSkuOrderByClientAndNullFinished(client);
			if(skuOrder == null){
				LOG.info(">>> adding skuorder ");
				skuOrder =  skuOrderRepository.save(new SkuOrder(client));
				LOG.info(">>> added skuorder ");
			}
			LOG.info(">>> adding skuOrderItem " );
			skuOrderItem = skuOrderItemRepository.save(new SkuOrderItem(skuOrder,sku, quantity));
			LOG.info(">>> added skuOrderItem " );
        } catch (DataIntegrityViolationException e){
            LOG.warn("Some constraints were throw due offer creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        } catch(Exception t){
            LOG.warn("Some constraints were throw due offer creation", t);
            throw new ConstraintsViolationException(t.getMessage());
        }
		
		return skuOrderItem;
	}
	
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
	@Transactional(rollbackFor = Exception.class)
	public SkuOrder totalOrderByClient(Long idClient) throws IllegalArgumentException, ConstraintsViolationException{ 
		if(idClient == null || idClient <= 0){
			throw new IllegalArgumentException("Invalid client identifier !");
		}
		
		Client client = clientRepository.findOne(idClient);
		
		if(client ==  null){
			throw new IllegalArgumentException("Invalid client identifier !");
		}
		
		SkuOrder skuOrder = skuOrderRepository.findSkuOrderByClientAndNullFinished(client);
		
		if(skuOrder == null){
			throw new IllegalArgumentException("There is no check-outs for this client !");
		}
		
		try{
			List<SkuOrderItem> items = skuOrderItemRepository.countSkuOrderByClient(skuOrder);
			LOG.info(">>> items " + items);
			Map<Sku, Double> skusQtd =  
					items.stream().collect(Collectors.groupingBy(SkuOrderItem::getSku, 
							               Collectors.summingDouble(SkuOrderItem::getQuantity)));
			skusQtd.forEach((sku, qtd) -> {
				LOG.info(">>> Sku :" + sku.getId() + " qtd : " + qtd);
				Promotion promo = promotionRepository.findPromocaoByIdSku(sku.getId());
				if(promo != null){
					if(qtd > promo.getQtdMinimal()){
						double qtdItensWithPromo    = qtd.longValue() / promo.getQtdMinimal().longValue();
						double qtdItensWithout      = qtd % promo.getQtdMinimal();
						double totalWithDiscount    = qtdItensWithPromo * promo.getPrice();
						double totalWithOutDiscount = qtdItensWithout * sku.getPrice();
								
						skuOrder.setAmoutPay(skuOrder.getAmoutPay() + totalWithDiscount + totalWithOutDiscount);
						
						double totalDiscount = (qtd * sku.getPrice()) - (totalWithDiscount + totalWithOutDiscount);
						
						skuOrder.setTotalDiscount(skuOrder.getTotalDiscount() + totalDiscount);
						
					} else {
						skuOrder.setAmoutPay(skuOrder.getAmoutPay() + qtd * sku.getPrice());
					}
					
				} else {
					skuOrder.setAmoutPay(skuOrder.getAmoutPay() + qtd * sku.getPrice());
				}
				skuOrder.setQuantity(skuOrder.getQuantity() + qtd);
				skuOrder.setTotalBeforeDiscount(skuOrder.getTotalBeforeDiscount() + qtd * sku.getPrice());
			});
			skuOrder.setFinishedDate(ZonedDateTime.now());
			skuOrderRepository.save(skuOrder);
        } catch (DataIntegrityViolationException e){
            LOG.warn("Some constraints were throw due offer creation", e);
            throw new ConstraintsViolationException(e.getMessage());
        } catch(Exception t){
            LOG.warn("Some constraints were throw due offer creation", t);
            throw new ConstraintsViolationException(t.getMessage());
        }
		return skuOrder;
	}
}
