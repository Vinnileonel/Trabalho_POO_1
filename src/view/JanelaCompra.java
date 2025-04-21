package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.time.LocalDate;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.TitledBorder;
import javax.swing.Vector;

import controller.Cartao;
import controller.Venda;
import model.Inventario;
import model.ProdutoInfo;

/**
 * Janela que simula uma caixa das lojas da cadeia HonESTa.
 */
public class JanelaCompra extends JFrame {
    private static final int ALTURA_JANELA = 600;
    private static final int LARGURA_JANELA = RendererListaInventario.DIM_BASE.width * 2
            + RendererListaVenda.DIM_BASE.width + 50;
    private static final Font ftMuitoGrande = new Font("Arial", Font.BOLD, 22);
    private static final Font ftGrande = new Font("Arial", Font.BOLD, 12);
    private static final Font ftMedia = ftGrande.deriveFont(Font.PLAIN, 12);
    private static final Font ftLista = new Font("Monospaced", Font.BOLD, 12);

    private DefaultListModel<ProdutoInfo> vendaModel;
    private JLabel totalLbl;
    private Venda vendaAtual;  // agora vamos iniciar no construtor

    public JanelaCompra(String title, Inventario inv) throws HeadlessException {
        super(title);
        this.vendaAtual = new Venda();               // <--- iniciamos uma nova venda
        setupJanela(inv);
    }

    private void adicionarProdutoVenda(ProdutoInfo p) {
        vendaAtual.adicionarProduto(p);              // <--- adiciona à venda
        vendaModel.addElement(p);                    // mostra na lista
        atualizarPrecoTotal(vendaAtual.getTotal());  // atualiza total
    }

    private void pagarComCartao(Cartao c) {
        if (!c.estaAtivo()) {                        // <--- verificar ativação
            JOptionPane.showMessageDialog(this, "Por favor, ative cartão na aplicação!");
            return;
        }

        long totalVenda = vendaAtual.getTotal();     // total com descontos aplicados
        int numeroCupoesUsados = vendaAtual.getCupoesUsados().size();
        long saldoCartao = c.getSaldo();             // saldo atual no cartão

        if (saldoCartao > 0) {
            int opcao = JOptionPane.showConfirmDialog(this,
                    "Deseja usar o saldo de " + precoToString(saldoCartao) + "?", "Usar saldo",
                    JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                c.reduzirSaldo(totalVenda);
            }
        }

        c.usar(vendaAtual);                           // <--- aplicar cupões e acumular saldo
        String mensagem = "<html>Obrigado pela sua compra de " + precoToString(totalVenda);
        if (numeroCupoesUsados > 1)
            mensagem += "<br>Usou " + numeroCupoesUsados + " cupões.";
        else if (numeroCupoesUsados == 1)
            mensagem += "<br>Usou 1 cupão.";
        JOptionPane.showMessageDialog(this, mensagem);

        vendaAtual = new Venda();                     // iniciar nova venda
        vendaModel.clear();                           // limpar lista e total
        atualizarPrecoTotal(0);
    }

    private long getPrecoProduto(ProdutoInfo p) {
        return p.getPrecoAtual();                     // <--- preço atual do produto
    }

    private String getMarcaProduto(ProdutoInfo p) {
        return p.getMarca();                          // <--- marca
    }

    private String getModeloProduto(ProdutoInfo p) {
        return p.getModelo();                         // <--- modelo
    }

    private final class RendererCartoes extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Cartao c = (Cartao) value;
            String numeroCartao = c.getNumero();      // <--- número do cartão
            return super.getListCellRendererComponent(list, numeroCartao, index, isSelected, cellHasFocus);
        }
    }

    private void setupJanela(Inventario inv) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LARGURA_JANELA, ALTURA_JANELA);
        setResizable(false);

        JPanel inventario = setupInventario(inv.todosProdutos());       // <--- passa a lista de produtos
        JPanel compra    = setupCompra(inv.todosCartoes());             // <--- passa a coleção de cartões

        getContentPane().add(inventario, BorderLayout.CENTER);
        getContentPane().add(compra, BorderLayout.WEST);
    }

    private void atualizarPrecoTotal(long total) {
        totalLbl.setText(precoToString(total));
    }

    private String precoToString(long total) {
        return String.format("%.2f€", total / 100f);
    }

    private JPanel setupInventario(Collection<ProdutoInfo> prods) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(RendererListaInventario.DIM_BASE.width * 2 + 10, ALTURA_JANELA));

        DefaultListModel<ProdutoInfo> produtosModel = new DefaultListModel<>();
        produtosModel.addAll(prods);
        JList<ProdutoInfo> produtos = new JList<>(produtosModel);
        produtos.setMaximumSize(new Dimension(RendererListaInventario.DIM_BASE.width * 2, ALTURA_JANELA + 30));
        produtos.setLayoutOrientation(JList.VERTICAL_WRAP);
        produtos.setVisibleRowCount(-1);
        produtos.setCellRenderer(new RendererListaInventario());
        produtos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        produtos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || produtos.getSelectedValue() == null)
                    return;
                adicionarProdutoVenda(produtos.getSelectedValue());
                produtos.clearSelection();
            }
        });

        panel.add(new JScrollPane(produtos));
        return panel;
    }

    private JPanel setupCompra(Collection<Cartao> cartoes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(RendererListaVenda.DIM_BASE.width + 20, ALTURA_JANELA));

        totalLbl = new JLabel("0.00€");
        totalLbl.setFont(ftMuitoGrande);
        totalLbl.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(totalLbl, BorderLayout.NORTH);

        JPanel vendaPnl = new JPanel(new BorderLayout());
        vendaPnl.setBorder(new TitledBorder("Produtos comprados"));
        vendaModel = new DefaultListModel<>();
        JList<ProdutoInfo> vendaList = new JList<>(vendaModel);
        vendaList.setEnabled(false);
        vendaList.setCellRenderer(new RendererListaVenda());
        vendaPnl.add(new JScrollPane(vendaList));
        panel.add(vendaPnl, BorderLayout.CENTER);

        JPanel cardPnl = new JPanel(new GridLayout(0, 1));
        JComboBox<Cartao> cartoesList = new JComboBox<>(new java.util.Vector<>(cartoes));
        cartoesList.setRenderer(new RendererCartoes());
        cardPnl.add(cartoesList);

        JButton pagaBt = new JButton("Pagar");
        pagaBt.addActionListener(l -> pagarComCartao((Cartao) cartoesList.getSelectedItem()));
        cardPnl.add(pagaBt);

        panel.add(cardPnl, BorderLayout.SOUTH);
        return panel;
    }

    // --- renderers antigos (não mexer) ---
    private final class RendererListaInventario extends DefaultListCellRenderer {
        private static final Dimension DIM_BASE = new Dimension(150, 40);
        private ProdutoInfo produto;

        @Override public Dimension getPreferredSize() { return DIM_BASE; }
        @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            produto = (ProdutoInfo) value;
            return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.drawRoundRect(2, 2, DIM_BASE.width - 4, DIM_BASE.height - 4, 8, 8);
            g.setFont(ftGrande);
            g.drawString(getMarcaProduto(produto), 5, 15);
            g.setFont(ftMedia);
            g.drawString(getModeloProduto(produto), 5, 35);
            g.drawString(precoToString(getPrecoProduto(produto)), 90, 15);
        }
    }

    private final class RendererListaVenda extends DefaultListCellRenderer {
        private static final int MAXIMO_LINHA = 25;
        private static final Dimension DIM_BASE = new Dimension(260, 20);
        private ProdutoInfo produto;
        private String getMarcaModeloProduto(ProdutoInfo p) {
            return getMarcaProduto(p) + " " + getModeloProduto(p);
        }
        @Override public Dimension getPreferredSize() { return DIM_BASE; }
        @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            produto = (ProdutoInfo) value;
            return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(ftLista);
            String descricao = getMarcaModeloProduto(produto);
            if (descricao.length() > MAXIMO_LINHA)
                descricao = descricao.substring(0, MAXIMO_LINHA - 3) + "...";
            String linha = String.format("%-" + MAXIMO_LINHA + "s %6.2f",
                    descricao, getPrecoProduto(produto) / 100f);
            g.drawString(linha, 5, 15);
        }
    }
}
