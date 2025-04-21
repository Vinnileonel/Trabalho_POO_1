package model;

import controller.Cartao;
import controller.Cupao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * “Base de dados” da aplicação HonESta.
 * <p>Guarda produtos, cartões e cupões em memória.</p>
 */
public class Inventario {

    /* ---------------------- armazenamento interno ---------------------- */
    private final Map<String, ProdutoInfo> produtos = new HashMap<>();
    private final Map<String, Cartao>      cartoes  = new HashMap<>();
    private final Map<String, Cupao>       cupoes   = new HashMap<>();

    /* --------------------------- INSERÇÃO ------------------------------ */

    public void addProduto(ProdutoInfo p){
        produtos.put(p.getCodigoBarras(), p);
    }

    public void addCupao(Cupao c){
        cupoes.put(c.getNumero(), c);
    }

    public void addCartao(Cartao c){
        cartoes.put(c.getNumero(), c);
    }

    /* --------------------------- PESQUISA ------------------------------ */

    /**
     * Procura um produto pelo código de barras.
     * @param codigoBarras código de barras a procurar.
     * @return ProdutoInfo ou {@code null} se não existir.
     */
    public ProdutoInfo getProduto(String codigoBarras) {
        return produtos.get(codigoBarras);
    }

    /**
     * Procura um cartão pelo número.
     * @param numero número do cartão.
     * @return Cartao ou {@code null} se não existir.
     */
    public Cartao getCartao(String numero) {
        return cartoes.get(numero);
    }

    /**
     * Procura um cupão pelo número.
     * @param numero número do cupão.
     * @return Cupao ou {@code null} se não existir.
     */
    public Cupao getCupao(String numero) {
        return cupoes.get(numero);
    }

    /* -------------- getters úteis para a interface gráfica ------------- */

    public Collection<ProdutoInfo> todosProdutos(){ return produtos.values(); }
    public Collection<Cartao>      todosCartoes() { return cartoes.values(); }
    public Collection<Cupao>       todosCupoes() { return cupoes.values(); }
}
