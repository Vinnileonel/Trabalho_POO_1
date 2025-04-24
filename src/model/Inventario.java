package model;

import controller.Cartao;
import controller.Cupao;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * “Base de dados” da aplicação HonESta.
 * Guarda produtos, cartões e cupões em memória.
 */
public class Inventario {

    private final Map<String, ProdutoInfo> produtos = new HashMap<>();
    private final Map<String, Cartao> cartoes   = new HashMap<>();
    private final Map<String, Cupao>  cupoes    = new HashMap<>();

    public void addProduto(ProdutoInfo p) {
        produtos.put(p.getCodigoBarras(), p);
    }

    public void addCupao(Cupao c) {
        cupoes.put(c.getNumero(), c);
    }

    public void addCartao(Cartao c) {
        cartoes.put(c.getNumero(), c);
    }

    public ProdutoInfo getProduto(String codigoBarras) {
        return produtos.get(codigoBarras);
    }

    public Cartao getCartao(String numero) {
        return cartoes.get(numero);
    }

    public Cupao getCupao(String numero) {
        return cupoes.get(numero);
    }

    public Collection<ProdutoInfo> todosProdutos() { return produtos.values(); }
    public Collection<Cartao>      todosCartoes()  { return cartoes.values(); }
    public Collection<Cupao>       todosCupoes()   { return cupoes.values(); }
}
