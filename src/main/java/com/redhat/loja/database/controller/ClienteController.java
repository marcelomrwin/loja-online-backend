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
import com.redhat.loja.repository.ClienteRepository;
import com.redhat.loja.service.ClienteService;
import com.redhat.loja_online.Cliente;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(origins = "*")
@Api(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON,value = "API de Clientes")
public class ClienteController {

	private final ClienteRepository repository;
	private final ClienteService service;

	public ClienteController(ClienteRepository repository,ClienteService service) {
		this.repository = repository;
		this.service = service;
	}

	@GetMapping
	@ApiOperation(produces = MediaType.APPLICATION_JSON,value = "Listar todos os clientes")
	public List<Cliente> getAll() {
		return service.listarTodos();
	}

	@GetMapping("/{id}")
	public Cliente get(@PathVariable("id") Long id) {
		verifyClienteExists(id);
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Cliente post(@RequestBody(required = false) Cliente cliente) {

		return repository.save(cliente);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public Cliente put(@PathVariable("id") Long id, @RequestBody(required = false) Cliente cliente) {
		verifyClienteExists(id);

		cliente.setId(id);
		return repository.save(cliente);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		verifyClienteExists(id);

		repository.deleteById(id);
	}

	private void verifyClienteExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException(String.format("Cliente with id=%d was not found", id));
		}
	}

}
