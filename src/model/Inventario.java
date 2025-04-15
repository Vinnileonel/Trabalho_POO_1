package model;

import controller.Cartao;
import controller.Cupao;

/**
 * Classe que representa o inventário da empresa, desde os produtos, até aos
 * cartões e cupões. Responsável por armazenar todos esses dados, bem como
 * fornecer métodos de pesquisa que usem os códigos para descobrir os produtos.
 */
public class Inventario {
    /**
     * Retorna qual o produto que tem um dado código de barras
     * 
     * @param codigoBarras o código de barras do produto que se pretende
     * @return o produto com o código de barras, ou null caso não exista
     */
    public ProdutoInfo getProduto(String codigoBarras) {
        // TODO implementar este método
        return null;
    }

    /**
     * Retorna o cartão com um dado número
     * 
     * @param numero o número do cartão a procurar
     * @return o cartão com o número pedido, ou null caso não exista
     */
    public Cartao getCartao(String numero) {
        // TODO implementar este método
        return null;
    }

    /**
     * Retorna o cupão com um dado número
     * 
     * @param numero o número do cupão a procurar
     * @return o cupão com o número pedido, ou null caso não exista
     */
    public Cupao getCupao(String numero) {
        // TODO implementar este método
        return null;
    }
}
