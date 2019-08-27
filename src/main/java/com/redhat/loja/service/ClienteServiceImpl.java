package com.redhat.loja.service;

import org.springframework.stereotype.Service;

import com.redhat.loja.repository.ClienteRepository;
import com.redhat.loja_online.Cliente;

@Service
public class ClienteServiceImpl extends AbstractCrudService<Cliente, Long> implements ClienteService {

	public ClienteServiceImpl(ClienteRepository repository) {
		super(repository);
	}

}
