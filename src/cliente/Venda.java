package cliente;

/**
 * Classe responsável por armazenar os produtos vendidos numa venda. Tem a data
 * em que foi realizada, os produtos e os cupões aplicados.
 */
public class Venda {
    private String data;
    private Produto[] produtos;
    private Cupao[] cupoes;
    private int numProdutos;
    private int numCupoes;
    private static final int MAX_PRODUTOS = 100;
    private static final int MAX_CUPOES = 10;
    private static final double DESCONTO_MAXIMO = 0.5; // 50% de desconto máximo
    private static final double DESCONTO_MINIMO = 0.0; // 0% de desconto mínimo
    
    /**
     * Construtor da classe Venda. Inicializa a data, os produtos e os cupões.
     * 
     * @param data Data da venda
     */
    public Venda(String data) {
        this.data = data;
        this.produtos = new Produto[MAX_PRODUTOS];
        this.cupoes = new Cupao[MAX_CUPOES];
        this.numProdutos = 0;
        this.numCupoes = 0;

        
}
