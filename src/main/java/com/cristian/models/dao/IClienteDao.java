package com.cristian.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cristian.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {
	
	
}
