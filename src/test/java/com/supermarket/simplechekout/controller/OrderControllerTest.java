package com.supermarket.simplechekout.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan.Filter;
//import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.supermarket.simplechekout.domin.Client;
import com.supermarket.simplechekout.domin.Sku;
import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;
import com.supermarket.simplechekout.dto.PurchaseSkuDTO;
import com.supermarket.simplechekout.service.SkuOrderService;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="test")
@WebMvcTest(controllers = {OrderController.class})
public class OrderControllerTest { 

	@Autowired
	private WebApplicationContext context;
	
	@MockBean
	private SkuOrderService skuOrderService;
	
	private MockMvc mockMvc;

	private Jackson2ObjectMapperBuilder builder;
	
	private String uri;
	
	private Client client;
	
	private Sku sku;
	
	private Double valueBase = 10.5D;
	

	public OrderControllerTest() {
		builder = new Jackson2ObjectMapperBuilder();
		client = new Client(1L, "client name");
		sku = new Sku(1L,"Sku",10.5D);
	}
	
	@Before
	public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc  = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void shouldCheckOutItem() throws Exception {
		uri = "/rest/order/new";
		PurchaseSkuDTO purchaseSkuDTO = new PurchaseSkuDTO(1L,2D, client.getClientId());
		final String requestBody = builder.build().writerWithDefaultPrettyPrinter().writeValueAsString(purchaseSkuDTO);
		SkuOrderItem skuOrderItem = new SkuOrderItem(new SkuOrder(client), sku, purchaseSkuDTO.getQtdSku());
		
		when(skuOrderService.addOrderItem(purchaseSkuDTO.getClientId(), 
		                                  purchaseSkuDTO.getIdSku(),
		                                  purchaseSkuDTO.getQtdSku())).thenReturn(skuOrderItem);        
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	public void shoutNotCheckOutItemDueNegativeSkuId() throws Exception {
		uri = "/rest/order/new";
		PurchaseSkuDTO purchaseSkuDTO = new PurchaseSkuDTO(-1L,2D, client.getClientId());
		final String requestBody = builder.build().writerWithDefaultPrettyPrinter().writeValueAsString(purchaseSkuDTO);
		when(skuOrderService.addOrderItem(purchaseSkuDTO.getClientId(), 
		                                  purchaseSkuDTO.getIdSku(),
		                                  purchaseSkuDTO.getQtdSku())).thenThrow(new IllegalArgumentException());        
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shoutNotCheckOutItemDueNegativeClientId() throws Exception {
		uri = "/rest/order/new";
		PurchaseSkuDTO purchaseSkuDTO = new PurchaseSkuDTO(sku.getId(),2D, -1L);
		final String requestBody = builder.build().writerWithDefaultPrettyPrinter().writeValueAsString(purchaseSkuDTO);
		when(skuOrderService.addOrderItem(purchaseSkuDTO.getClientId(), 
		                                  purchaseSkuDTO.getIdSku(),
		                                  purchaseSkuDTO.getQtdSku())).thenThrow(new IllegalArgumentException());        
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	@Test
	public void shoutNotCheckOutItemDueNegativeQuantity() throws Exception{
		uri = "/rest/order/new";
		PurchaseSkuDTO purchaseSkuDTO = new PurchaseSkuDTO(sku.getId(),-2D, client.getClientId());
		final String requestBody = builder.build().writerWithDefaultPrettyPrinter().writeValueAsString(purchaseSkuDTO);
		when(skuOrderService.addOrderItem(purchaseSkuDTO.getClientId(), 
		                                  purchaseSkuDTO.getIdSku(),
		                                  purchaseSkuDTO.getQtdSku())).thenThrow(new IllegalArgumentException());        
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shouldGetTotalcheckOutByClient() throws Exception {
		int clientId = 1;
		uri = "/rest/order/" + clientId;
		when(skuOrderService.totalOrderByClient(anyLong())).
		   thenReturn(new SkuOrder(1L,client, valueBase, valueBase, valueBase));
		
        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.totalNumberItens").value(equalTo(valueBase)));
	}

	@Test
	public void shouldNotGetTotalcheckOutByClientDueNegativeClientId() throws Exception {
		int clientId = -1;
		uri = "/rest/order/" + clientId;
		when(skuOrderService.totalOrderByClient(anyLong())).
		   thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shouldHandleJsonParseException() throws Exception {
		final String content = "{\"id_sku\":\"-1\",\"qtd_sku\":\"10\",\"id_client\"\"10\"}";
		uri = "/rest/order/new";
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
		
	}
	
	@Test
	public void shouldHandleJsonMappingException() throws Exception {
		final String content = "{\"idsku\":\"-1\",\"qtdsku\":\"10\",\"id_client\"\"10\"}";
		uri = "/rest/order/new";
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
		
	}


}
