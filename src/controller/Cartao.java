package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Cartão de fidelização da cadeia HonESta.
 * <p>Os preços/saldos são guardados em cêntimos (long).</p>
 */
public class Cartao {

    /* ---------- atributos ---------- */
    private final String numero;
    private long saldo;                              // em cêntimos
    private final List<Cupao> listaCupoes;           // cupões atribuídos ao cliente
    private final List<Cupao> ativos;                // cupões escolhidos p/ esta compra
    private boolean ativo;                           // cartão “ligado” na app

    /* ---------- construtor ---------- */
    public Cartao(String numero, long saldoInicial) {
        this.numero = numero;
        this.saldo  = saldoInicial;
        this.listaCupoes = new ArrayList<>();
        this.ativos      = new ArrayList<>();
        this.ativo = false;
    }

    /* -----------------------------------------------------------
     * 1º método pedido — IMPLEMENTADO
     * -----------------------------------------------------------
     */

    /**
     * Ativa os cupões selecionados.
     * <ul>
     *   <li>todos têm de pertencer ao cartão;</li>
     *   <li>todos têm de estar dentro do prazo de validade;</li>
     *   <li>se passar nas validações, ficam registados em {@code ativos}
     *       e o cartão passa a estar em uso.</li>
     * </ul>
     *
     * @param selecionar lista de cupões a ativar pelo cliente
     * @throws IllegalArgumentException se a lista estiver vazia,
     *                                  contiver cupões que não pertençam
     *                                  ou contenha algum expirado.
     */
    public void ativar(List<Cupao> selecionar) {

        if (selecionar == null || selecionar.isEmpty())
            throw new IllegalArgumentException("Lista vazia");

        /* 1) todos pertencem ao cartão? */
        if (!listaCupoes.containsAll(selecionar))
            throw new IllegalArgumentException("Cupão não pertence ao cartão");

        /* 2) todos válidos hoje? */
        boolean expirado = selecionar.stream().anyMatch(c -> !c.estaValido());
        if (expirado)
            throw new IllegalArgumentException("Cupão fora de validade");

        /* 3) regista cupões ativos e liga o cartão */
        ativos.clear();
        ativos.addAll(selecionar);
        ativo = true;
    }

    /* -----------------------------------------------------------
     *  Novo método: adicionarCupoes
     * -----------------------------------------------------------
     */

    /**
     * Atribui ao cartão uma coleção de cupões.
     * Evita duplicados e aceita qualquer Collection.
     *
     * @param novos cupões a adicionar ao cartão
     */
    public void adicionarCupoes(Collection<Cupao> novos) {
        if (novos == null) return;
        for (Cupao c : novos) {
            if (!listaCupoes.contains(c)) {
                listaCupoes.add(c);
            }
        }
    }

    /* ===========================================================
     *  Métodos ainda por implementar — deixam‑se com TODO
     * =========================================================== */

    public void usar(Venda v) {
        // TODO implementar este método
    }

    public List<Cupao> getCupoesDisponiveis() {
        // TODO implementar este método
        return null;
    }

    public List<Cupao> getCupoesFuturos() {
        // TODO implementar este método
        return null;
    }

    public void atualizarCupoes() {
        // TODO implementar este método
    }

    public void reduzirSaldo(long gasto) {
        // TODO implementar este método
    }

    public void acumularSaldo(long valor) {
        // TODO implementar este método
    }

    /** @return {@code true} se o cartão está ativo na app do cliente. */
    public boolean estaAtivo() {
        return ativo;
    }

    /* getters auxiliares caso precises noutras classes */
    public String getNumero()          { return numero; }
    public long   getSaldo()           { return saldo; }
    public List<Cupao> getListaCupoes(){ return listaCupoes; }
    public List<Cupao> getAtivos()     { return ativos; }
}
