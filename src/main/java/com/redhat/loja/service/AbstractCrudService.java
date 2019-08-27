package com.redhat.loja.service;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

public class AbstractCrudService<T, ID> implements CrudServices<T> {

	protected final CrudRepository<T, ID> repository;

	public AbstractCrudService(CrudRepository<T, ID> repository) {
		this.repository = repository;
	}

	@Override
	public List<T> listarTodos() {

		Spliterator<T> all = repository.findAll().spliterator();

		return StreamSupport.stream(all, false).collect(Collectors.toList());

	}

}
