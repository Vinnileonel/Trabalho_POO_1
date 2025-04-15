package cliente;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe que representa um cupão emitido pela cadeia de lojas HonESta. Este
 * cupão pode ser associado a um ou mais cartões de fidelização. Cada cupão dá
 * direito a um desconto (em cartão) na compra dos produtos que abrange.
 */
public class Cupao {
    private final String numero; // número do cupão
    private final String resumo; // do que é o cupão
    private final int desconto; // porcentagem e nao valor em centavos, coloquei final nesses tres porque eles nao vao mudar
    private LocalDate dataInicio; // data de início do cupão
    private LocalDate dataFim; // data de fim do cupão
    private ArrayList<String> codigoProduto = new ArrayList<String>(); // lista dos códigos do produduto

    
    // coonstrutor
    public Cupao(String numero, String resumo, int desconto, LocalDate dataInicio, LocalDate dataFim,
            ArrayList<String> codigoProduto) {
        this.numero = checkNumero(numero);
        this.resumo = checkResumo(resumo);
        this.desconto = checkDesconto(desconto);
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.codigoProduto = checkCodigoProduto(codigoProduto);
    }
    // getters e setters

    public String getNumero() {
        return numero;
    }

    public String getResumo() {
        return resumo;
    }

    public int getDesconto() {
        return desconto;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public ArrayList<String> getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(ArrayList<String> codigoProduto) {
        this.codigoProduto = checkCodigoProduto(codigoProduto);
    }
    //metodos pra checar, nao fiz para as datas porque ja sao do tipo LocalDate
    private ArrayList<String> checkCodigoProduto(ArrayList<String> codigoProduto) {
        if (codigoProduto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return codigoProduto;
    }
    private String checkNumero(String numero) {
        if (numero.isBlank()) {
            throw new IllegalArgumentException();
        }
        return numero;
    }
    private String checkResumo(String resumo) {
        if (resumo.isBlank()) {
            throw new IllegalArgumentException();
        }
        return resumo;
    }
    private int checkDesconto(int desconto) {
        if (desconto < 0 || desconto > 100) {
            throw new IllegalArgumentException();
        }
        return desconto;
    }   

    /**
     * Indica se o cupão está válido no dia de hoje, isto é, se o dia de hoje está
     * dentro do prazo de utilização.
     * 
     * @return true se está dentro do prazo de utilização
     */
    public boolean estaValido() {
        // Feito implementar este método
        LocalDate hoje = LocalDate.now();
        if(hoje.isAfter(dataInicio) && hoje.isBefore(dataFim) || hoje.isEqual(dataInicio) || hoje.isEqual(dataFim)) {
            return true;
        }
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
        // Feito implementar este método
        if(data.isAfter(dataInicio) && data.isBefore(dataFim) || data.isEqual(dataInicio) || data.isEqual(dataFim)) {
            return true;
        }
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
