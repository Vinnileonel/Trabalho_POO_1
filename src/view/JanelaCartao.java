package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

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
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import controller.Cartao;
import controller.Cupao;

/**
 * Janela que simula a aplicação do cliente, mostrando saldo e permitindo ativar cupões.
 */
public class JanelaCartao extends JFrame {
    private static final int ALTURA = 600;
    private static final int LARGURA = RendererCupoes.DIM_BASE.width + 70;
    private static final int ALTURA_ATUAIS = 4 * ALTURA / 7;
    private static final Font ftDesconto = new Font("Arial", Font.BOLD, 20);
    private static final Font ftTexto    = ftDesconto.deriveFont(Font.PLAIN, 14);
    private static final Font ftValidade = ftTexto.deriveFont(12f);

    private JLabel saldoLbl;
    private DefaultListModel<Cupao> cupoesAtuaisModel;
    private DefaultListModel<Cupao> cupoesProximosModel;
    private Cartao cardAtual;

    public JanelaCartao(String title, Collection<Cartao> cartoes) {
        super(title);

        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        setupGUI(cartoes);

        // Configurações da janela
        setSize(LARGURA, ALTURA);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupGUI(Collection<Cartao> cartoes) {
        // 1) Combo de cartões
        JComboBox<Cartao> combo = new JComboBox<>(new Vector<>(cartoes));
        combo.setRenderer(new RendererCartoes());
        combo.addActionListener(e -> mudarCartao((Cartao) combo.getSelectedItem()));
        getContentPane().add(combo, BorderLayout.NORTH);

        // 2) Painel central com SpringLayout
        JPanel panel = new JPanel(new SpringLayout());
        panel.setBorder(new TitledBorder("Dados do cartão"));
        SpringLayout layout = (SpringLayout) panel.getLayout();

        // Label de saldo
        JLabel lblSaldo = new JLabel("Saldo:");
        lblSaldo.setFont(ftTexto);
        panel.add(lblSaldo);

        saldoLbl = new JLabel("0.00€");
        saldoLbl.setFont(ftTexto);
        panel.add(saldoLbl);

        layout.putConstraint(SpringLayout.NORTH, lblSaldo, 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST,  lblSaldo, 5, SpringLayout.WEST,  panel);
        layout.putConstraint(SpringLayout.NORTH, saldoLbl, 0, SpringLayout.NORTH, lblSaldo);
        layout.putConstraint(SpringLayout.EAST,  saldoLbl, -5, SpringLayout.EAST, panel);

        // Lista de cupons atuais
        cupoesAtuaisModel = new DefaultListModel<>();
        JList<Cupao> listAtuais = new JList<>(cupoesAtuaisModel);
        listAtuais.setCellRenderer(new RendererCupoes());
        JPanel atuais = new JPanel(new BorderLayout());
        atuais.setBorder(new TitledBorder("Cupões em vigor"));
        atuais.add(new JScrollPane(listAtuais), BorderLayout.CENTER);
        panel.add(atuais);

        layout.putConstraint(SpringLayout.NORTH, atuais, 3, SpringLayout.SOUTH, lblSaldo);
        layout.putConstraint(SpringLayout.WEST,  atuais, 3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST,  atuais, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, atuais, ALTURA_ATUAIS, SpringLayout.NORTH, atuais);

        // Botão “Usar cartão”
        JButton bt = new JButton("Usar cartão");
        bt.addActionListener(e -> ativarCartao(listAtuais.getSelectedValuesList()));
        panel.add(bt);
        layout.putConstraint(SpringLayout.NORTH, bt, 3, SpringLayout.SOUTH, atuais);
        layout.putConstraint(SpringLayout.WEST,  bt, 3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST,  bt, -3, SpringLayout.EAST, panel);

        // Lista de cupons futuros
        cupoesProximosModel = new DefaultListModel<>();
        JList<Cupao> listFuturos = new JList<>(cupoesProximosModel);
        listFuturos.setCellRenderer(new RendererCupoes());
        listFuturos.setEnabled(false);
        JPanel proximos = new JPanel(new BorderLayout());
        proximos.setBorder(new TitledBorder("Próximos cupões"));
        proximos.add(new JScrollPane(listFuturos), BorderLayout.CENTER);
        panel.add(proximos);

        layout.putConstraint(SpringLayout.NORTH, proximos, 3, SpringLayout.SOUTH, bt);
        layout.putConstraint(SpringLayout.WEST,  proximos, 3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.EAST,  proximos, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, proximos, -3, SpringLayout.SOUTH, panel);

        getContentPane().add(panel, BorderLayout.CENTER);

        // Inicializa primeira seleção
        if (!cartoes.isEmpty()) {
            combo.setSelectedIndex(0);
            mudarCartao(combo.getItemAt(0));
        }
    }

    private void mudarCartao(Cartao c) {
        this.cardAtual = c;
        c.atualizarCupoes();
        saldoLbl.setText(String.format("%.2f€", c.getSaldo() / 100f));

        cupoesAtuaisModel.clear();
        cupoesAtuaisModel.addAll(c.getCupoesDisponiveis());

        cupoesProximosModel.clear();
        cupoesProximosModel.addAll(c.getCupoesFuturos());
    }

    private void ativarCartao(List<Cupao> selecionados) {
        cardAtual.ativar(selecionados);
        JOptionPane.showMessageDialog(
                this,
                "Cupons ativados com sucesso!",
                "Cartão ativado",
                JOptionPane.INFORMATION_MESSAGE
        );
        mudarCartao(cardAtual);
    }

    private void desenharCupao(Graphics g, RendererCupoes r, Cupao cupao) {
        // aqui chamamos o método do renderer interno para desenhar
        r.paintDadosCupao(
                g,
                cupao.getResumo(),
                cupao.getPercentual() / 100f,
                cupao.getDataInicio(),
                cupao.getDataFim()
        );
    }

    // --- Renderers internos ---

    private final class RendererCartoes extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            Cartao c = (Cartao) value;
            return super.getListCellRendererComponent(
                    list, c.getNumero(), index, isSelected, cellHasFocus
            );
        }
    }

    private final class RendererCupoes extends DefaultListCellRenderer {
        static final Dimension DIM_BASE = new Dimension(200, 60);
        private Cupao cupao;

        @Override public Dimension getPreferredSize() {
            return DIM_BASE;
        }

        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            cupao = (Cupao) value;
            return super.getListCellRendererComponent(
                    list, "", index, isSelected, cellHasFocus
            );
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharCupao(g, this, cupao);
        }

        /**
         * Desenha o disco laranja, o texto do desconto, o resumo e a validade.
         */
        void paintDadosCupao(
                Graphics g,
                String resumo,
                float desconto,
                LocalDate inicio,
                LocalDate fim) {

            // fundo laranja
            g.setColor(Color.ORANGE);
            g.fillOval(2, 6, 45, 45);

            // contorno do cupão
            g.setColor(Color.BLACK);
            g.drawRoundRect(2, 2, DIM_BASE.width - 4, DIM_BASE.height - 4, 6, 6);

            // desconto (%)
            g.setFont(ftDesconto);
            String pct = String.format("%02d%%", (int)(desconto * 100));
            g.drawString(pct, 5, 35);

            // resumo do cupão
            g.setFont(ftTexto);
            g.drawString(resumo, 60, 20);

            // validade (dd/MM)
            g.setFont(ftValidade);
            String validade = String.format(
                    "%02d/%02d - %02d/%02d",
                    inicio.getDayOfMonth(), inicio.getMonthValue(),
                    fim.getDayOfMonth(),    fim.getMonthValue()
            );
            g.drawString(validade, 60, 50);
        }
    }
}
