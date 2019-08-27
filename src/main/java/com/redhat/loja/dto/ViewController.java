package com.redhat.loja.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.UnselectEvent;

import com.redhat.loja.repository.CompraRepository;
import com.redhat.loja.repository.ProdutoRepository;
import com.redhat.loja.service.ClienteService;
import com.redhat.loja.service.KieClientService;
import com.redhat.loja.service.ProdutoService;
import com.redhat.loja_online.Cliente;
import com.redhat.loja_online.Compra;
import com.redhat.loja_online.Produto;
import com.redhat.loja_online.ProdutoCompra;

import lombok.Data;

@Data
@Named
public class ViewController {

	private Compra compra = Compra.builder().produtos(new ArrayList<>()).totalDescontos(0.0).frete(0.0).valorTotal(0.0)
			.build();

	private List<Cliente> listaClientes = new ArrayList<>();
	private List<Cliente> filtroClientes = new ArrayList<>();
	private List<Produto> listaProdutos = new ArrayList<>();

	@Inject
	private ClienteService clienteService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private CompraRepository compraRepository;

	@Inject
	private ProdutoRepository produtoRepository;

	@Inject
	private KieClientService kieClient;

	private ProdutoCompra produtoCompraSelect;
	private Produto produtoSelect;
	private Integer quantity;

	@PostConstruct
	public void init() {
		listaClientes = clienteService.listarTodos();
		listaProdutos = produtoService.listarTodos();
	}

	public void excluirProduto() {
		compra.excluirProduto(produtoCompraSelect);
		// chama o DM para recalcular o novo valor da compra
		kieClient.calcularTotalCompra(compra);
		// resetar form
		reset();
	}

	public void adicionarProduto() {
		ProdutoCompra produtoCompra = ProdutoCompra.builder().uuid(UUID.randomUUID()).produto(produtoSelect)
				.quantidade(quantity).valorTotal(quantity * produtoSelect.getValorUnitario()).build();
		// adiciona o item na compra
		this.compra.adicionarProduto(produtoCompra);

		System.out.println("Antes de enviar para calculo\n======");
		System.out.println(this.compra.getFrete());
		System.out.println(this.compra.getTotalDescontos());
		System.out.println(this.compra.getValorTotal());
		System.out.println("==================\n");

		// chama o DM para recalcular o novo valor da compra
		this.compra = kieClient.calcularTotalCompra(compra);

		System.out.println("Após de enviar para calculo\n======");
		System.out.println(this.compra.getFrete());
		System.out.println(this.compra.getTotalDescontos());
		System.out.println(this.compra.getValorTotal());
		System.out.println("==================\n");

		// resetar form
		reset();
	}

	public void reset() {
		this.produtoSelect = null;
		this.quantity = 0;
		this.produtoCompraSelect = null;
	}

	public Double valorTotalDescontos() {
		
		return compra.valorTotalDescontos();
	}
	
	public Integer totalDeItens() {
		return compra.quantidadeDeItens();
	}

	public void onRowClienteUnselect(UnselectEvent event) {
		this.compra.setCliente(null);
	}
	
	public Double totalDosItens() {
		return compra.totalDosProdutos();
	}

}
