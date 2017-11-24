package com.supermarket.simplechekout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.simplechekout.domin.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
