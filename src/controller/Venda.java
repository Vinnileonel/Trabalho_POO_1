package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por armazenar os produtos vendidos numa venda. Tem a data
 * em que foi realizada, os produtos e os cupões aplicados.
 */
public class Venda {
    private LocalDate data;
    private List<ProdutoVendido> produtosVendidos;//é do tipo produto vendido porque pegamos da classe produto vendido a informação
    private List<Cupao> cupoesAplicados;//é do tipo cupao porque pegamos da classe cupao a informação
    
    public Venda() {
        this.data = LocalDate.now();//localDate.now();pq queremos a data da venda no momento
        this.produtosVendidos = new ArrayList<>();
        this.cupoesAplicados = new ArrayList<>();
    }
    
    public void adicionarCupaoAplicado(Cupao cupao) {
        cupoesAplicados.add(cupao);
    }
    
    public void adicionarProdutoVendido(ProdutoVendido produto) {
        produtosVendidos.add(produto);
    }
    
    public LocalDate getData() {
        return data;
    }

    public List<ProdutoVendido> getProdutosVendidos() {
        return produtosVendidos;
    }

    public List<Cupao> getCupoesAplicados() {
        return cupoesAplicados;
    }

    public long getPrecoTotal() { //metodo que percorre os produtos todos vendidos em uma compra e soma o preco de cada um deles
        long total = 0;
        for (ProdutoVendido produto : produtosVendidos) {
            total += produto.precoFim();
        }
        return total;
    }

}

