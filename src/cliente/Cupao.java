package cliente;

import java.time.LocalDate;

/**
 * Classe que representa um cupão emitido pela cadeia de lojas HonESta. Este
 * cupão pode ser associado a um ou mais cartões de fidelização. Cada cupão dá
 * direito a um desconto (em cartão) na compra dos produtos que abrange.
 */
public class Cupao {
    /**
     * Indica se o cupão está válido no dia de hoje, isto é, se o dia de hoje está
     * dentro do prazo de utilização.
     * 
     * @return true se está dentro do prazo de utilização
     */
    public boolean estaValido() {
        // TODO implementar este método
        return false;
    }

    /**
     * Indica se o cupão está válido num dado dia, isto é, se esse dia está dentro
     * do prazo de utilização.
     * 
     * @param data dia em que se pretende verificar se o cupão está válido
     * @return true se a data está dentro do prazo de utilização
     */
    public boolean estaValido(LocalDate data) {
        // TODO implementar este método
        return false;
    }

    /**
     * Aplica o cupão de desconto a um cartão e a uma venda. Se a venda contiver
     * algum dos produtos abrangidos pelo cupão, este é dado como tendo sido
     * aplicado. Se for aplicado deve ser removido do cartão.
     * 
     * @param c o cartão a ser usado na venda
     * @param v a venda a ser processada
     * @return true se o cupão foi aplicado na venda
     */
    public boolean aplicar(Cartao c, Venda v) {
        // TODO implementar este método
        return false;
    }

    /**
     * Indica se o produto indicado é abrangido pelo cupão. O produto é abrangido
     * pelo cupão se fizer parte da lista dos produtos e não tiver já sido aplicado
     * ao produto um desconto maior do que o dado pelo cupão.
     * 
     * @param p o produto a testar
     * @return true, se o produto é abrangido pela cupão
     */
    public boolean abrange(ProdutoVendido p) {
        // TODO implementar este método
        return false;
    }

    /**
     * Método auxiliar para aplicar o cupão a um produto.
     * 
     * @param c o cartão onde acumular o saldo
     * @param p o produto a ser usado
     */
    private void aplicar(Cartao c, ProdutoVendido p) {
        // TODO implementar este método
    }
}
