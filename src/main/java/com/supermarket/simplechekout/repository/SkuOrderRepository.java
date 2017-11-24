package com.supermarket.simplechekout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.supermarket.simplechekout.domin.Client;
import com.supermarket.simplechekout.domin.SkuOrder;

public interface SkuOrderRepository extends JpaRepository<SkuOrder, Long> {
	
	@Query("select s from SkuOrder s where s.client = :client and s.finishedDate is null")
	SkuOrder findSkuOrderByClientAndNullFinished(@Param("client") Client client);

}
