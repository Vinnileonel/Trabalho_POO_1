package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Armazena os produtos vendidos e os cupões aplicados numa venda.
 */
public class Venda {
    private LocalDate data;
    private List<ProdutoVendido> itensVendidos;
    private List<Cupao> cuponsAplicados;

    public Venda() {
        this.data = LocalDate.now();
        this.itensVendidos = new ArrayList<>();
        this.cuponsAplicados = new ArrayList<>();
    }

    /**
     * Adiciona um item (ProdutoVendido) à venda.
     */
    public void adicionarItem(ProdutoVendido pv) {
        itensVendidos.add(pv);
    }

    /**
     * Ordena os cupões ativos por percentual de desconto decrescente e aplica
     * o melhor a cada itemVendidos, registando os cupões efetivamente usados.
     */
    public void aplicarMelhoresCupons(List<Cupao> cuponsAtivos) {
        // 1. Ordena por desconto (%) decrescente
        cuponsAtivos.sort(Comparator.comparingInt(Cupao::getPercentual).reversed());

        // 2. Para cada produto, tenta aplicar o primeiro cupão válido que o abranja
        for (ProdutoVendido pv : itensVendidos) {
            for (Cupao c : new ArrayList<>(cuponsAtivos)) {
                if (c.estaValido() && c.abrange(pv)) {
                    long descontoCalculado = pv.getPreco() * c.getPercentual() / 100;
                    pv.setDesconto(descontoCalculado);
                    cuponsAplicados.add(c);
                    cuponsAtivos.remove(c);
                    break;
                }
            }
        }
    }

    /**
     * Soma dos preços originais (sem desconto) de todos os itens.
     */
    public long totalBruto() {
        return itensVendidos.stream()
                .mapToLong(ProdutoVendido::getPreco)
                .sum();
    }

    /**
     * Soma dos preços finais (já com desconto) de todos os itens.
     */
    public long totalLiquido() {
        return itensVendidos.stream()
                .mapToLong(ProdutoVendido::precoFim)
                .sum();
    }

    /**
     * Número de cupões que foram efetivamente aplicados nesta venda.
     */
    public int numeroDeCuponsUsados() {
        return cuponsAplicados.size();
    }

    // Getters

    /**
     * Data em que a venda foi criada.
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Itens vendidos (cada um com seu preço e eventual desconto).
     */
    public List<ProdutoVendido> getItensVendidos() {
        return itensVendidos;
    }

    /**
     * Lista de cupões aplicados nesta venda.
     */
    public List<Cupao> getCuponsAplicados() {
        return cuponsAplicados;
    }
}
