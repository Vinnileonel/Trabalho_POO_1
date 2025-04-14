package arranque;

import java.util.List;

import javax.swing.SwingUtilities;

import comercio.Inventario;
import menu.JanelaCartao;
import menu.JanelaCompra;

public class Main {

    public static void main(String[] args) {
        // criar o inventário e restantes elementos necessário à aplicação
        Inventario inventario = new Inventario();
        criarProdutos(inventario);
        criarCupoes(inventario);
        criarCartoes(inventario);

        // criar e apresentar as janelas da aplicação
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JanelaCompra jc = new JanelaCompra("Loja HonESta de Castelo Branco", inventario);
                jc.setLocation(20, 20);
                jc.setVisible(true);

                // TODO passar uma coleção de cartões para o inventário (em vez da lista vazia)
                JanelaCartao jcard = new JanelaCartao(List.of());

                jcard.setLocation(20 + jc.getWidth() + 20, 20);
                jcard.setVisible(true);
            }
        });
    }

    /**
     * Cria os produtos e adiciona-os ao inventário
     * 
     * @param inventario onde colocoar os produtos
     */
    private static void criarProdutos(Inventario inventario) {
        // TODO implementar este método
    }

    /**
     * Cria e configura os cupões e adiciona-os ao inventário
     * 
     * @param inventario onde colocoar os cupões
     */
    private static void criarCupoes(Inventario inventario) {
        // TODO implementar este método
    }

    /**
     * Cria e configura os cartões e adiciona-os ao inventário
     * 
     * @param inventario onde colocoar os cartões
     */
    private static void criarCartoes(Inventario inventario) {
        // TODO implementar este método
    }
}
