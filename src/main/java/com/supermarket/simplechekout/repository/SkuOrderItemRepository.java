package com.supermarket.simplechekout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.supermarket.simplechekout.domin.SkuOrder;
import com.supermarket.simplechekout.domin.SkuOrderItem;

public interface SkuOrderItemRepository extends JpaRepository<SkuOrderItem, Long> {

	@Query("select s from SkuOrderItem s where s.skuOrder = :skuOrder ")
	List<SkuOrderItem> countSkuOrderByClient(@Param("skuOrder") SkuOrder skuOrder);

	
}
