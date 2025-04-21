package controller;

import model.ProdutoInfo;

/**
 * Representa um produto vendido numa compra. Atenção, esta classe não contém a
 * informação sobre o produto propriamente dito (para isso usa um ProdutoInfo),
 * mas sim sobre as condições de venda desse produto, nomeadamente o preço usado
 * na altura e o desconto.
 */
public class ProdutoVendido {

    private ProdutoInfo produto;    // ProdutoInfo do produto vendido
    private long preco;             // Preço do produto na altura da venda       
    private long desconto;          // Desconto aplicado ao produto na altura da venda  

    // construtor
    public ProdutoVendido(ProdutoInfo produto, long preco, long desconto) {
        this.produto = produto;
        this.preco = checkPreco(preco);
        this.desconto = checkDesconto(desconto);
    }

     // getters e setters, tirei os setters de alguns por sererm imutaveis
    public ProdutoInfo getProduto() {
        return produto;
    }

    public long getPreco() {
        return preco;
    }

    public long getDesconto() {
        return desconto;
    }

    public void setDesconto(long desconto) {
        this.desconto = checkDesconto(desconto);
    }
    public String getCodigoBarras() {
        return produto.getCodigoBarras();
    }
    public String getMarca() {
        return produto.getMarca();
    }
    public String getModelo() {
        return produto.getModelo();
    }
    //toString
    @Override
    public String toString() {
        return "ProdutoVendido [produto=" + produto + ", preco=" + preco + ", desconto=" + desconto + "]";
    }
    //metodos check
    private long checkPreco(long preco) {
        if (preco < 0) {
            throw new IllegalArgumentException();
        }
        return preco;
    }
    private long checkDesconto(long desconto) {
        if (desconto < 0) {
            throw new IllegalArgumentException();
        }
        return desconto;
    }
    
    public long precoFim() {
        return preco - desconto;
    }


             
}

