package controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa um cupão de desconto da cadeia HonESTa.
 */
public class Cupao {
    private final String numero;
    private final String resumo;
    private final int percentual;            // em %
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final List<String> produtos;     // códigos de barras imutáveis

    public Cupao(String numero,
                 String resumo,
                 int percentual,
                 LocalDate dataInicio,
                 LocalDate dataFim,
                 List<String> produtos) {
        this.numero      = Objects.requireNonNull(numero, "número não pode ser null").strip();
        if (this.numero.isEmpty()) throw new IllegalArgumentException("número não pode ser vazio");

        this.resumo      = Objects.requireNonNull(resumo, "resumo não pode ser null").strip();
        if (this.resumo.isEmpty()) throw new IllegalArgumentException("resumo não pode ser vazio");

        if (percentual < 0 || percentual > 100)
            throw new IllegalArgumentException("percentual inválido: " + percentual);
        this.percentual  = percentual;

        this.dataInicio  = Objects.requireNonNull(dataInicio, "dataInicio não pode ser null");
        this.dataFim     = Objects.requireNonNull(dataFim, "dataFim não pode ser null");
        if (dataInicio.isAfter(dataFim))
            throw new IllegalArgumentException("dataInicio não pode ser após dataFim");

        Objects.requireNonNull(produtos, "lista de produtos não pode ser null");
        if (produtos.isEmpty())
            throw new IllegalArgumentException("lista de produtos não pode estar vazia");
        this.produtos    = Collections.unmodifiableList(List.copyOf(produtos));
    }

    public String getNumero() {
        return numero;
    }

    public String getResumo() {
        return resumo;
    }

    /**
     * Percentual de desconto (0–100).
     */
    public int getPercentual() {
        return percentual;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    /**
     * Códigos de barras dos produtos abrangidos por este cupão.
     */
    public List<String> getProdutos() {
        return produtos;
    }

    /**
     * @return true se hoje estiver entre dataInicio e dataFim (inclusive).
     */
    public boolean estaValido() {
        LocalDate hoje = LocalDate.now();
        return (!hoje.isBefore(dataInicio)) && (!hoje.isAfter(dataFim));
    }

    /**
     * @return true se o cupão abrange o produto (igual ao seu código de barras).
     */
    public boolean abrange(ProdutoVendido pv) {
        Objects.requireNonNull(pv, "ProdutoVendido não pode ser null");
        return produtos.contains(pv.getCodigoBarras());
    }

    /**
     * Aplica este cupão ao produto, ajustando o desconto em cêntimos.
     * @return true se foi aplicado (válido e abrange o produto), false caso contrário.
     */
    public boolean aplicar(ProdutoVendido pv) {
        if (!estaValido() || !abrange(pv)) return false;
        long valorDesconto = pv.getPreco() * percentual / 100;
        pv.setDesconto(valorDesconto);
        return true;
    }

    @Override
    public String toString() {
        return String.format("Cupão %s [%d%%] ‒ %s (%s a %s)",
                numero, percentual, resumo, dataInicio, dataFim);
    }
}
