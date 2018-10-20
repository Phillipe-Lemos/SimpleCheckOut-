package com.supermarket.simplechekout.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.supermarket.simplechekout.domin.Client;
import com.supermarket.simplechekout.domin.Sku;
import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.exception.ConstraintsViolationException;
import com.supermarket.simplechekout.repository.ClientRepository;
import com.supermarket.simplechekout.repository.PromotionRepository;
import com.supermarket.simplechekout.repository.SkuOrderItemRepository;
import com.supermarket.simplechekout.repository.SkuOrderRepository;
import com.supermarket.simplechekout.repository.SkuRepository;
import com.supermarket.simplechekout.service.impl.SkuOrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SkuOrderServiceTest {
	
	@Mock
	private SkuRepository skuRepository;
	
	@Mock
	private SkuOrderRepository skuOrderRepository;
	
	@Mock
	private SkuOrderItemRepository skuOrderItemRepository;
	
	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private PromotionRepository promotionRepository;
	
	private SkuOrderService skuOrderService;
	
	private Optional<Client> buildMockClient(Long id, String name) {
		return Optional.of(new Client(id, name));
	}
	
	private Optional<Sku> buildSku(Long id, String skuName, Double price) {
		return Optional.of(new Sku(id, skuName, price));
	}
	
	private SkuOrder buildSkuOrder(Long id, Client client, Double quantity, Double totalDiscount, Double amountPay) {
		return new SkuOrder(id, client, quantity, totalDiscount, amountPay); 
	}
	
	private List<SkuOrderItem> buildMockListSkuOrderItem(SkuOrder skuOrder, Sku sku) {
		return Arrays.asList(
				    new SkuOrderItem(skuOrder, sku, 10D),
				    new SkuOrderItem(skuOrder, sku, 11D),
				    new SkuOrderItem(skuOrder, sku, 12D),
				    new SkuOrderItem(skuOrder, sku, 13D)
				);
				
	}
	
	@Before
	public void setUp() {
		skuOrderService = new SkuOrderServiceImpl(skuRepository, 
                                                  skuOrderRepository, 
                                                  clientRepository,
                                                  skuOrderItemRepository,
                                                  promotionRepository);
	}
	
	@Test
	public void addOrderItemSuccess() {
		long skuId = 1L; 
		long clienId = 1L; 
		Optional<Client> client = buildMockClient(clienId, "client_1");
		Optional<Sku> sku = buildSku(skuId, "sku_1", 10D);
		Double quantity = 10D;
		SkuOrder skuOrder = buildSkuOrder(1L, client.get(), quantity, 10D, 50D);
		
		when(clientRepository.findById(anyLong())).thenReturn(client);
		
		when(skuRepository.findById(anyLong())).thenReturn(sku);
		
		when(skuOrderRepository.findSkuOrderByClientAndNullFinished(client.get()))
		   .thenReturn(skuOrder);
		
		SkuOrderItem mockSkuOrdemItem = new SkuOrderItem(skuOrder, sku.get(), quantity);
		
		SkuOrderItem expectedValue = new SkuOrderItem(skuOrder, sku.get(), quantity);
		expectedValue.setId(1L);
		
		when(skuOrderItemRepository.save(mockSkuOrdemItem))
				.thenReturn(expectedValue);
		
		SkuOrderItem result =  skuOrderService.addOrderItem(1L, 1L, quantity);
		assertThat(result, notNullValue());
		assertThat(expectedValue, equalTo(result));
	}
	
	@Test(expected=ConstraintsViolationException.class)
	public void shouldNotAddOrderItemDueConstraintViolation() {
		long skuId = 1L; 
		long clienId = 1L; 
		Optional<Client> client = buildMockClient(clienId, "client_1");
		Optional<Sku> sku = buildSku(skuId, "sku_1", 10D);
		Double quantity = 10D;
		SkuOrder skuOrder = buildSkuOrder(1L, client.get(), quantity, 10D, 50D);
		
		when(clientRepository.findById(anyLong())).thenReturn(client);
		
		when(skuRepository.findById(anyLong())).thenReturn(sku);
		
		when(skuOrderRepository.findSkuOrderByClientAndNullFinished(client.get()))
		   .thenReturn(skuOrder);
		
		SkuOrderItem mockSkuOrdemItem = new SkuOrderItem(skuOrder, sku.get(), quantity);
		
		SkuOrderItem expectedValue = new SkuOrderItem(skuOrder, sku.get(), quantity);
		expectedValue.setId(1L);
		
		when(skuOrderItemRepository.save(mockSkuOrdemItem))
				.thenThrow(RuntimeException.class);
		
		skuOrderService.addOrderItem(1L, 1L, quantity);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNegativeClientId() {
		skuOrderService.addOrderItem(-1L, 1L, 10D);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNullClientId() {
		skuOrderService.addOrderItem(null, 1L, 10D);
		fail();
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNullSkuId() {
		skuOrderService.addOrderItem(1L, null, 10D);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNegativeSkuId() {
		skuOrderService.addOrderItem(1L, -1L, 10D);
		fail();
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNegativeQuantity() {
		skuOrderService.addOrderItem(1L, 1L, -1D);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAddItemDueNullQuantity() {
		skuOrderService.addOrderItem(1L, 1L, null);
		fail();
	}
	
	@Test
	public void totalOrderByClientSuccess() {
		long skuId = 1L; 
		long clientId = 1L; 
		Optional<Client> client = buildMockClient(clientId, "client_1");
		Optional<Sku> sku = buildSku(skuId, "sku_1", 10D);
		Double quantity = 10D;
		SkuOrder skuOrder = buildSkuOrder(1L, client.get(), quantity, 10D, 50D);

		when(clientRepository.findById(anyLong())).thenReturn(client);
		
		when(skuOrderRepository.findSkuOrderByClientAndNullFinished(client.get()))
		   .thenReturn(skuOrder);

		when(skuOrderItemRepository.countSkuOrderByClient(skuOrder))
		   .thenReturn(buildMockListSkuOrderItem(skuOrder, sku.get()));
		
		when(skuOrderRepository.save(skuOrder)).thenReturn(skuOrder);
		
		SkuOrder result = skuOrderService.totalOrderByClient(clientId);
		assertThat(result, notNullValue());
		assertThat(skuOrder, equalTo(result));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotSummarizeDueNegativeClientId() {
		skuOrderService.totalOrderByClient(-1L);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotSummarizeDueNullClientId() {
		skuOrderService.totalOrderByClient(null);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotSummarizesDueCheckoutsAbsense() {
		long skuId = 2L; 
		long clientId = 2L; 
		
		Optional<Client> client = buildMockClient(clientId, "client_2");
		
		Optional<Sku> sku = buildSku(skuId, "sku_1", 10D);
		
		Double quantity = 10D;
		
		when(clientRepository.findById(anyLong())).thenReturn(client);
		
		when(skuOrderRepository.findSkuOrderByClientAndNullFinished(client.get()))
		   .thenReturn(null);

		skuOrderService.totalOrderByClient(clientId);
		fail();
	}
	
	@Test(expected=ConstraintsViolationException.class)
	public void shouldNotSummariesDueTriggerSavedConstraint() {
		long skuId = 1L; 
		long clientId = 1L; 
		Optional<Client> client = buildMockClient(clientId, "client_1");
		Optional<Sku> sku = buildSku(skuId, "sku_1", 10D);
		Double quantity = 10D;
		SkuOrder skuOrder = buildSkuOrder(1L, client.get(), quantity, 10D, 50D);

		when(clientRepository.findById(anyLong())).thenReturn(client);
		
		when(skuOrderRepository.findSkuOrderByClientAndNullFinished(client.get()))
		   .thenReturn(skuOrder);

		when(skuOrderItemRepository.countSkuOrderByClient(skuOrder))
		   .thenReturn(buildMockListSkuOrderItem(skuOrder, sku.get()));
		
		when(skuOrderRepository.save(skuOrder)).thenThrow(RuntimeException.class);
		
		skuOrderService.totalOrderByClient(clientId);
		fail();
	}
	
}
