package controller;

import model.ProdutoInfo;

/**
 * Representa um produto vendido numa venda, com preço e desconto aplicados.
 */
public class ProdutoVendido {
    private ProdutoInfo produto;
    private long preco;
    private long desconto;

    public ProdutoVendido(ProdutoInfo produto, long preco, long desconto) {
        this.produto  = produto;
        this.preco    = checkPreco(preco);
        this.desconto = checkDesconto(desconto);
    }

    public ProdutoInfo getProduto()    { return produto; }
    public long getPreco()             { return preco; }
    public long getDesconto()          { return desconto; }
    public void setDesconto(long d)    { this.desconto = checkDesconto(d); }

    public String getCodigoBarras()    { return produto.getCodigoBarras(); }
    public String getMarca()           { return produto.getMarca();       }
    public String getModelo()          { return produto.getModelo();      }

    private long checkPreco(long p) {
        if (p < 0) throw new IllegalArgumentException();
        return p;
    }
    private long checkDesconto(long d) {
        if (d < 0 || d > preco)
            throw new IllegalArgumentException("Desconto inválido: " + d);
        return d;
    }

    public long precoFim() {
        return preco - desconto;
    }

    @Override
    public String toString() {
        return "ProdutoVendido [produto=" + produto +
               ", preco=" + preco +
               ", desconto=" + desconto + "]";
    }
}
