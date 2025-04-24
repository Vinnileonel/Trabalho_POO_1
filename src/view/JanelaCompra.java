package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
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
import javax.swing.border.TitledBorder;

import controller.Cartao;
import controller.ProdutoVendido;
import controller.Venda;
import model.Inventario;
import model.ProdutoInfo;

public class JanelaCompra extends JFrame {
    private static final int ALTURA_JANELA = 600;
    private static final int LARGURA_JANELA = RendererListaInventario.DIM_BASE.width * 2
            + RendererListaVenda.DIM_BASE.width + 50;
    private static final Font ftMuitoGrande = new Font("Arial", Font.BOLD, 22);
    private static final Font ftLista = new Font("Monospaced", Font.BOLD, 12);

    private DefaultListModel<ProdutoVendido> vendaModel;
    private JLabel totalLbl;
    private Venda vendaAtual;

    /**
     * @param title Título a mostrar na barra da janela
     * @param inv   Inventário com produtos e cartões
     */
    public JanelaCompra(String title, Inventario inv) throws HeadlessException {
        super(title);               // define o título
        this.vendaAtual = new Venda();
        setupJanela(inv);

        pack();                     // ajusta ao conteúdo
        setLocationRelativeTo(null);// centra no ecrã
    }

    private void adicionarProdutoVenda(ProdutoInfo p) {
        ProdutoVendido pv = new ProdutoVendido(p, p.getPrecoAtual(), 0);
        vendaAtual.adicionarItem(pv);
        vendaModel.addElement(pv);
        atualizarPrecoTotal(vendaAtual.totalLiquido());
    }

    private void pagarComCartao(Cartao c) {
        if (!c.estaAtivo()) {
            JOptionPane.showMessageDialog(this, "Por favor, ative o cartão na aplicação!");
            return;
        }

        c.usar(vendaAtual);

        long totalVenda = vendaAtual.totalLiquido();
        int numeroCupoesUsados = vendaAtual.numeroDeCuponsUsados();
        long saldoCartao = c.getSaldo();

        if (saldoCartao > 0 && totalVenda > 0) {
            int opcao = JOptionPane.showConfirmDialog(this,
                    "Deseja usar o saldo de " + precoToString(saldoCartao) + "?", "Usar saldo",
                    JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                c.reduzirSaldo(totalVenda);
            }
        }

        StringBuilder msg = new StringBuilder();
        msg.append("Obrigado pela sua compra de ").append(precoToString(totalVenda));
        if (numeroCupoesUsados > 1) {
            msg.append("\nUsou ").append(numeroCupoesUsados).append(" cupões.");
        } else if (numeroCupoesUsados == 1) {
            msg.append("\nUsou 1 cupão.");
        }
        JOptionPane.showMessageDialog(this, msg.toString());

        vendaAtual = new Venda();
        vendaModel.clear();
        atualizarPrecoTotal(0);
    }

    private String precoToString(long total) {
        return String.format("%.2f€", total / 100f);
    }

    private void atualizarPrecoTotal(long total) {
        totalLbl.setText(precoToString(total));
    }

    private void setupJanela(Inventario inv) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel inventario = setupInventario(inv.todosProdutos());
        JPanel compra     = setupCompra(inv.todosCartoes());

        getContentPane().add(inventario, BorderLayout.CENTER);
        getContentPane().add(compra, BorderLayout.WEST);
    }

    private JPanel setupInventario(Collection<ProdutoInfo> prods) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(RendererListaInventario.DIM_BASE.width * 2 + 10, ALTURA_JANELA));

        DefaultListModel<ProdutoInfo> produtosModel = new DefaultListModel<>();
        produtosModel.addAll(prods);
        JList<ProdutoInfo> produtos = new JList<>(produtosModel);
        produtos.setLayoutOrientation(JList.VERTICAL_WRAP);
        produtos.setVisibleRowCount(-1);
        produtos.setCellRenderer(new RendererListaInventario());
        produtos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        produtos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && produtos.getSelectedValue() != null) {
                adicionarProdutoVenda(produtos.getSelectedValue());
                produtos.clearSelection();
            }
        });

        panel.add(new JScrollPane(produtos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel setupCompra(Collection<Cartao> cartoes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(RendererListaVenda.DIM_BASE.width + 20, ALTURA_JANELA));

        totalLbl = new JLabel("0.00€");
        totalLbl.setFont(ftMuitoGrande);
        totalLbl.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(totalLbl, BorderLayout.NORTH);

        DefaultListModel<ProdutoVendido> model = new DefaultListModel<>();
        vendaModel = model;
        JList<ProdutoVendido> vendaList = new JList<>(model);
        vendaList.setEnabled(false);
        vendaList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                ProdutoVendido pv = (ProdutoVendido) value;
                String texto = String.format("%s %s  %6.2f€",
                        pv.getMarca(), pv.getModelo(),
                        pv.precoFim() / 100f);
                return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
            }
        });

        JPanel vendaPnl = new JPanel(new BorderLayout());
        vendaPnl.setBorder(new TitledBorder("Produtos comprados"));
        vendaPnl.add(new JScrollPane(vendaList), BorderLayout.CENTER);
        panel.add(vendaPnl, BorderLayout.CENTER);

        JPanel cardPnl = new JPanel(new GridLayout(0, 1));
        JComboBox<Cartao> cartoesList = new JComboBox<>(new java.util.Vector<>(cartoes));
        cartoesList.setRenderer(new RendererCartoes());
        cardPnl.add(cartoesList);

        JButton pagaBt = new JButton("Pagar");
        pagaBt.addActionListener(l ->
                pagarComCartao(cartoesList.getItemAt(cartoesList.getSelectedIndex()))
        );
        cardPnl.add(pagaBt);

        panel.add(cardPnl, BorderLayout.SOUTH);
        return panel;
    }

    // --- inner classes ---

    private final class RendererCartoes extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            Cartao c = (Cartao) value;
            return super.getListCellRendererComponent(list, c.getNumero(), index, isSelected, cellHasFocus);
        }
    }

    private final class RendererListaInventario extends DefaultListCellRenderer {
        private static final Dimension DIM_BASE = new Dimension(150, 40);
        private ProdutoInfo produto;

        @Override public Dimension getPreferredSize() { return DIM_BASE; }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            produto = (ProdutoInfo) value;
            return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.drawRoundRect(2, 2, DIM_BASE.width - 4, DIM_BASE.height - 4, 8, 8);
            g.setFont(ftLista);
            g.drawString(produto.getMarca(), 5, 15);
            g.drawString(produto.getModelo(), 5, 35);
            g.drawString(precoToString(produto.getPrecoAtual()), 90, 15);
        }
    }

    private final class RendererListaVenda extends DefaultListCellRenderer {
        static final Dimension DIM_BASE = new Dimension(260, 20);
        @Override public Dimension getPreferredSize() { return DIM_BASE; }
    }
}
