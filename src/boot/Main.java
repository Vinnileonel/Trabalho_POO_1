package boot;

import controller.Cartao;
import controller.Cupao;
import model.Inventario;
import model.ProdutoInfo;
import view.JanelaCartao;
import view.JanelaCompra;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Arranque da aplicação HonESta.
 */
public class Main {

    private static long €(double valor) {
        return Math.round(valor * 100);
    }

    public static void main(String[] args) {
        Inventario inv = new Inventario();
        criarProdutos(inv);
        criarCupoes(inv);
        criarCartoes(inv);

        SwingUtilities.invokeLater(() -> {
            JanelaCompra jc = new JanelaCompra("Loja HonESTa – Castelo Branco", inv);
            jc.setVisible(true);
            jc.setLocation(20, 20);

            JanelaCartao jCard = new JanelaCartao("App Cliente – HonESTa", inv.todosCartoes());
            jCard.setVisible(true);
            jCard.setLocation(20 + jc.getWidth() + 20, 20);

        });

    }

    private static void criarProdutos(Inventario inv) {
        inv.addProduto(new ProdutoInfo("123-001","EST","BarraMix maçã canela", €(0.89)));
        inv.addProduto(new ProdutoInfo("123-002","EST","CeriCrisp",            €(1.49)));
        inv.addProduto(new ProdutoInfo("123-003","EST","Sumix Limonada",       €(1.49)));
        inv.addProduto(new ProdutoInfo("123-004","EST","Chocolate c/ amêndoas",€(1.49)));
        inv.addProduto(new ProdutoInfo("123-005","EST","Arroz agulha",         €(1.09)));
        inv.addProduto(new ProdutoInfo("123-006","EST","Arroz carolino",       €(1.19)));
        inv.addProduto(new ProdutoInfo("123-007","EST","Arroz basmati",        €(1.69)));

        inv.addProduto(new ProdutoInfo("222-001","AlbiCereal","Arroz Agulha",     €(1.69)));
        inv.addProduto(new ProdutoInfo("222-002","AlbiCereal","Arroz Carolino",   €(1.79)));
        inv.addProduto(new ProdutoInfo("222-003","AlbiCereal","Céu Estrelado",     €(2.19)));
        inv.addProduto(new ProdutoInfo("222-004","AlbiCereal","Aveia crunch",      €(2.49)));
        inv.addProduto(new ProdutoInfo("222-005","AlbiCereal","Massa esparguete", €(0.99)));
        inv.addProduto(new ProdutoInfo("222-006","AlbiCereal","Massa macarronete", €(1.09)));

        inv.addProduto(new ProdutoInfo("301-001","DoceVida","Chocolate de leite",   €(1.49)));
        inv.addProduto(new ProdutoInfo("301-002","DoceVida","Chocolate c/ avelã",   €(1.69)));
        inv.addProduto(new ProdutoInfo("301-003","DoceVida","Chocolate negro 70%",  €(1.89)));

        inv.addProduto(new ProdutoInfo("404-001","Refrescate","Sumo de maçã",      €(1.39)));
        inv.addProduto(new ProdutoInfo("404-002","Refrescate","Sumo de laranja",   €(1.39)));
        inv.addProduto(new ProdutoInfo("404-003","Refrescate","Sumo de ananás",    €(1.39)));

        // Produtos extra definidos pelo grupo:
        inv.addProduto(new ProdutoInfo("555-001","EST","Granola Mix frutos",      €(2.29)));
        inv.addProduto(new ProdutoInfo("666-001","AlbiCereal","Muesli Tropic",    €(2.59)));
    }

    /*------------------------------------------------------*
     * 2) CUPÕES
     *------------------------------------------------------*/
    private static void criarCupoes(Inventario inv) {
        LocalDate hoje = LocalDate.now();

        inv.addCupao(new Cupao("1001","em refrigerantes",50,
                hoje, hoje.plusDays(7),
                new ArrayList<>(List.of("123-003","404-001","404-002","404-003"))));

        inv.addCupao(new Cupao("1002","em chocolates",25,
                hoje, hoje.plusDays(7),
                new ArrayList<>(List.of("123-004","301-001","301-002","301-003"))));

        inv.addCupao(new Cupao("1003","em massa",25,
                hoje, hoje.plusDays(7),
                new ArrayList<>(List.of("222-005","222-006"))));

        inv.addCupao(new Cupao("1004","em arroz",25,
                hoje, hoje.plusDays(7),
                new ArrayList<>(List.of("123-005","123-006","123-007",
                        "222-001","222-002"))));

        inv.addCupao(new Cupao("1005","na marca EST",15,
                hoje, hoje.plusDays(7),
                new ArrayList<>(List.of("123-001","123-002","123-003","123-004",
                        "123-005","123-006","123-007"))));

        inv.addCupao(new Cupao("1006","em cereais de pequeno‑almoço",25,
                hoje.plusDays(8), hoje.plusDays(15),
                new ArrayList<>(List.of("123-002","222-003","222-004"))));

        // Cupão futuro extra
        inv.addCupao(new Cupao("1007","em granola & muesli",20,
                hoje.plusDays(10), hoje.plusDays(20),
                new ArrayList<>(List.of("555-001","666-001"))));
    }

    /*------------------------------------------------------*
     * 3) CARTÕES
     *------------------------------------------------------*/
    private static void criarCartoes(Inventario inv) {
        // Cartão 10101
        Cartao c1 = new Cartao("10101", 0);
        c1.adicionarCupoes(List.of(
                inv.getCupao("1001"), inv.getCupao("1003"),
                inv.getCupao("1005"), inv.getCupao("1006")));
        inv.addCartao(c1);

        // Cartão 20202
        Cartao c2 = new Cartao("20202", €(2.50));
        c2.adicionarCupoes(List.of(
                inv.getCupao("1001"), inv.getCupao("1002"),
                inv.getCupao("1004"), inv.getCupao("1006")));
        inv.addCartao(c2);

        // Cartão 30303
        Cartao c3 = new Cartao("30303", €(0.58));
        c3.adicionarCupoes(List.of(
                inv.getCupao("1002"), inv.getCupao("1003"),
                inv.getCupao("1004"), inv.getCupao("1005"),
                inv.getCupao("1006")));
        inv.addCartao(c3);
    }
}
