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
import com.redhat.loja.repository.CompraRepository;
import com.redhat.loja_online.Compra;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/compras")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*")
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON,value = "API de Compras")
public class CompraController {

	private final CompraRepository repository;

	public CompraController(CompraRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	@ApiOperation(produces = MediaType.APPLICATION_JSON,value = "Listar todos as compras")
	public List<Compra> getAll() {
		Spliterator<Compra> compras = repository.findAll().spliterator();

		return StreamSupport.stream(compras, false).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public Compra get(@PathVariable("id") Long id) {
		verifyCompraExists(id);
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Compra post(@RequestBody(required = false) Compra compra) {

		return repository.save(compra);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public Compra put(@PathVariable("id") Long id, @RequestBody(required = false) Compra compra) {
		verifyCompraExists(id);

		compra.setId(id);
		return repository.save(compra);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		verifyCompraExists(id);

		repository.deleteById(id);
	}

	private void verifyCompraExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException(String.format("Compra with id=%d was not found", id));
		}
	}

}
