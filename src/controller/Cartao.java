package cliente;

import java.util.List;

/**
 * Classe que representa um cartão de fidelização na cadeia de lojas HonESta.
 */
public class Cartao {

    /**
     * Ativa os cupões selecionados. Não aceita cupões que não estejam na lista de
     * cupões do cliente
     * 
     * @param ativos lista de cupões para ativar
     */
    public void ativar(List<Cupao> ativos) {
        // TODO implementar este método
    }

    /**
     * Usar o cartão numa venda, se este estiver ativo. Se tiver cupões ativos estes
     * devem ser aplicados também. Os cupões usados na venda serão removidos. Os
     * cupões ativos mas não usados na venda, deixam de estar ativos, mas permanecem
     * associados ao cartão. No final do uso o cartão é automaticamente desativado.
     * 
     * @param v a venda onde usar o cartão
     * @throws IllegalStateException se o cartão não estiver ativo
     */
    public void usar(Venda v) {
        // TODO implementar este método
    }

    /**
     * Retorna uma lista com os cupões disponíveis. Os cupões disponíveis são
     * aqueles que estão válidos no dia atual.
     * 
     * @return uma lista com os cupões disponíveis
     */
    public List<Cupao> getCupoesDisponiveis() {
        // TODO implementar este método
        return null;
    }

    /**
     * Retorna uma lista com os cupões que estarão disponíveis no futuro, isto é,
     * cujo início é após o dia de hoje.
     * 
     * @return uma lista com os cupões que estarão disponíveis no futuro
     */
    public List<Cupao> getCupoesFuturos() {
        // TODO implementar este método
        return null;
    }

    /**
     * Atualiza os cupões, removendo os que já passaram de validade
     */
    public void atualizarCupoes() {
        // TODO implementar este método
    }

    /**
     * Usar o saldo do cartão. Deve garantir que o gasto não é maior que o saldo.
     * 
     * @param gasto o que retirar do saldo.
     */
    public void reduzirSaldo(long gasto) {
        // TODO implementar este método
    }

    /**
     * Acumular saldo no cartão
     * 
     * @param valor valor a acumular no saldo
     */
    public void acumularSaldo(long valor) {
        // TODO implementar este método
    }

    /**
     * indica se um cartão está ativo. isto é, pronto a ser usado numa compra.
     * 
     * @return true se está ativo.
     */
    public boolean estaAtivo() {
        // TODO implementar este método
        return true;
    }
}
