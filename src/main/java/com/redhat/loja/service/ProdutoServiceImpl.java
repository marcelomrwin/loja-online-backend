package com.redhat.loja.service;

import org.springframework.stereotype.Service;

import com.redhat.loja.repository.ProdutoRepository;
import com.redhat.loja_online.Produto;

@Service
public class ProdutoServiceImpl extends AbstractCrudService<Produto, Long> implements ProdutoService {

	public ProdutoServiceImpl(ProdutoRepository repository) {
		super(repository);
	}

}
