package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cartão de fidelização da cadeia HonESta.
 * Preços/saldos são guardados em cêntimos (long).
 */
public class Cartao {
    private final String numero;
    private long saldo;
    private final List<Cupao> listaCupoes;
    private final List<Cupao> ativos;
    private boolean ativo;

    public Cartao(String numero, long saldoInicial) {
        this.numero      = Objects.requireNonNull(numero).strip();
        if (this.numero.isEmpty()) throw new IllegalArgumentException("Número inválido");
        this.saldo       = saldoInicial;
        this.listaCupoes = new ArrayList<>();
        this.ativos      = new ArrayList<>();
        this.ativo       = false;
    }

    /**
     * Ativa os cupões selecionados (devem pertencer ao cartão e estar dentro do prazo).
     */
    public void ativar(List<Cupao> selecionar) {
        if (selecionar == null || selecionar.isEmpty())
            throw new IllegalArgumentException("Nenhum cupão selecionado");
        if (!listaCupoes.containsAll(selecionar))
            throw new IllegalArgumentException("Cupão não pertence ao cartão");
        if (selecionar.stream().anyMatch(c -> !c.estaValido()))
            throw new IllegalArgumentException("Há cupões fora de validade");
        ativos.clear();
        ativos.addAll(selecionar);
        ativo = true;
    }

    /**
     * Adiciona novos cupões ao cartão (ignorando duplicados).
     */
    public void adicionarCupoes(List<Cupao> novos) {
        if (novos == null) return;
        for (Cupao c : novos) {
            if (!listaCupoes.contains(c)) {
                listaCupoes.add(c);
            }
        }
    }

    /**
     * Executa a venda: aplica os melhores cupões, acumula desconto em saldo
     * e limpa os cupões usados/ativos.
     */
    public void usar(Venda v) {
        // 1) aplica cupões aos itens
        v.aplicarMelhoresCupons(ativos);
        // 2) calcula quanto virou saldo (bruto – líquido)
        long ganho = v.totalBruto() - v.totalLiquido();
        acumularSaldo(ganho);
        // 3) remove os cupões efetivamente usados
        listaCupoes.removeAll(v.getCuponsAplicados());
        // 4) limpa estado de ativo
        ativos.clear();
        ativo = false;
    }

    /** @return os cupões cujo prazo já começou e não expirou. */
    public List<Cupao> getCupoesDisponiveis() {
        LocalDate hoje = LocalDate.now();
        List<Cupao> ret = new ArrayList<>();
        for (Cupao c : listaCupoes) {
            if (!hoje.isBefore(c.getDataInicio()) && !hoje.isAfter(c.getDataFim())) {
                ret.add(c);
            }
        }
        return ret;
    }

    /** @return os cupões que só estarão válidos no futuro. */
    public List<Cupao> getCupoesFuturos() {
        LocalDate hoje = LocalDate.now();
        List<Cupao> ret = new ArrayList<>();
        for (Cupao c : listaCupoes) {
            if (hoje.isBefore(c.getDataInicio())) {
                ret.add(c);
            }
        }
        return ret;
    }

    /** Remove dos cupões todos os que já expiraram. */
    public void atualizarCupoes() {
        LocalDate hoje = LocalDate.now();
        listaCupoes.removeIf(c -> hoje.isAfter(c.getDataFim()));
        ativos.removeIf(c -> hoje.isAfter(c.getDataFim()));
        if (ativos.isEmpty()) ativo = false;
    }

    /** Reduz o saldo (não vai a negativo). */
    public void reduzirSaldo(long gasto) {
        if (gasto < 0) throw new IllegalArgumentException("Gasto inválido");
        if (gasto >= saldo) saldo = 0;
        else saldo -= gasto;
    }

    /** Acrescenta ao saldo (valor >= 0). */
    public void acumularSaldo(long valor) {
        if (valor < 0) throw new IllegalArgumentException("Valor inválido");
        saldo += valor;
    }

    public boolean estaAtivo()      { return ativo; }
    public String  getNumero()       { return numero; }
    public long    getSaldo()        { return saldo;   }
    public List<Cupao> getListaCupoes() { return listaCupoes; }
    public List<Cupao> getAtivos()      { return ativos;      }
}
