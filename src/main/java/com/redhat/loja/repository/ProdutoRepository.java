package com.redhat.loja.repository;

import org.springframework.data.repository.CrudRepository;

import com.redhat.loja_online.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {
}
