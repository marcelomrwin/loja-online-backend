package com.redhat.loja_online;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Compra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cliente cliente;

	@OneToMany
	private java.util.List<ProdutoCompra> produtos;

	private java.lang.Double frete;

	private java.time.LocalDateTime data;

	private java.lang.Double valorTotal;

	private java.lang.Double totalDescontos;

	public Integer quantidadeDeItens() {
		Integer qtd = 0;
		if (this.produtos != null && !this.produtos.isEmpty())
			for (ProdutoCompra produtoCompra : produtos) {
				qtd += produtoCompra.getQuantidade();
			}
		return qtd;
	}

	public Double totalDosProdutos() {
		Double valor = 0.0;
		if (this.produtos != null && !this.produtos.isEmpty())
			for (ProdutoCompra produtoCompra : produtos) {
				valor += produtoCompra.getValorTotal();
			}
		return valor;
	}

	public Double valorTotalDescontos() {
		Double value = totalDosProdutos();
		value *= totalDescontos;
		return value;
	}

	public void aplicarDescontoPercentual(Double percentual) {
		double percent = percentual / 100;
		this.valorTotal = this.valorTotal - (percent * this.valorTotal);
	}

	public void excluirProduto(ProdutoCompra produto) {
		this.produtos.remove(produto);
	}

	public void adicionarProduto(ProdutoCompra produto) {
		this.produtos.add(produto);
	}
}