package com.supermarket.simplechekout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.simplechekout.domin.Sku;

public interface SkuRepository extends JpaRepository<Sku, Long> {

}
