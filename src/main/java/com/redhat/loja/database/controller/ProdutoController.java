package com.redhat.loja.database.controller;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.redhat.loja.database.exception.NotFoundException;
import com.redhat.loja.repository.ProdutoRepository;
import com.redhat.loja_online.Produto;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*")
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, value = "API de Produtos")
public class ProdutoController {

	private final ProdutoRepository repository;

	public ProdutoController(ProdutoRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Produto> getAll() {
		Spliterator<Produto> produtos = repository.findAll().spliterator();

		return StreamSupport.stream(produtos, false).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public Produto get(@PathVariable("id") Long id) {
		verifyProdutoExists(id);
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Produto post(@RequestBody(required = false) Produto produto) {

		return repository.save(produto);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public Produto put(@PathVariable("id") Long id, @RequestBody(required = false) Produto produto) {
		verifyProdutoExists(id);

		produto.setId(id);
		return repository.save(produto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		verifyProdutoExists(id);

		repository.deleteById(id);
	}

	private void verifyProdutoExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException(String.format("Produto with id=%d was not found", id));
		}
	}

}
