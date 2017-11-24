package com.supermarket.simplechekout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.supermarket.simplechekout.domin.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	@Query("select p from Promotion p where p.sku.id = :idSku")
	Promotion findPromocaoByIdSku(@Param("idSku") Long idSku);
	
}
