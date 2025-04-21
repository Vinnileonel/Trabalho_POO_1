package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import controller.Cartao;
import controller.Cupao;

/**
 * Janela que simula a aplicação do cliente onde estes podem ver e ativar
 * os cupões do seu cartão.
 */
public class JanelaCartao extends JFrame {

    private static final int ALTURA = 600;
    private static final int COMPRIMENTO = RendererCupoes.DIM_BASE.width + 70;
    private static final int ALTURA_ATUAIS = 4 * ALTURA / 7;
    private static final Font ftDesconto = new Font("Arial", Font.BOLD, 20);
    private static final Font ftTexto    = ftDesconto.deriveFont(Font.PLAIN, 14);
    private static final Font ftValidade = ftTexto.deriveFont(12f);

    private JLabel saldoLbl;
    private DefaultListModel<Cupao> cupoesAtuaisModel;
    private DefaultListModel<Cupao> cupoesProximosModel;
    private Cartao cardAtual;

    public JanelaCartao(Collection<Cartao> cartoes) {
        setupGUI(cartoes);
    }

    public void mudarCartao(Cartao c) {
        this.cardAtual = c;
        c.atualizarCupoes();                  // limpa expirados

        long saldo = c.getSaldo();            // valor do saldo
        List<Cupao> atuais = c.getCupoesDisponiveis();
        List<Cupao> futuros = c.getCupoesFuturos();

        updateSaldo(saldo);
        updateCupoesAtuais(atuais);
        updateCupoesProximos(futuros);
    }

    public void desenharCupao(Graphics g, RendererCupoes r, Cupao cupao) {
        String resumo    = cupao.getDescricao();
        float desconto   = cupao.getPercentagem() / 100f;
        LocalDate inicio = cupao.getInicio();
        LocalDate fim    = cupao.getFim();
        r.paintDadosCupao(g, resumo, desconto, inicio, fim);
    }

    private void ativarCartao(List<Cupao> selecionados) {
        cardAtual.ativar(selecionados);       // ativa cupões
        mostrarMensagem("Cartão ativo", "Pode usar o cartão");
        mudarCartao(cardAtual);               // refresca saldo e listas
    }

    private final class RendererCartoes extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Cartao c = (Cartao) value;
            String numero = c.getNumero();     // mostra o número do cartão
            return super.getListCellRendererComponent(list, numero, index, isSelected, cellHasFocus);
        }
    }

    private void updateSaldo(long saldo) {
        saldoLbl.setText(String.format("%.2f€", saldo / 100.0f));
    }

    private void updateCupoesAtuais(List<Cupao> cupoesAtuais) {
        cupoesAtuaisModel.clear();
        cupoesAtuaisModel.addAll(cupoesAtuais);
    }

    private void updateCupoesProximos(List<Cupao> cupoesProximos) {
        cupoesProximosModel.clear();
        cupoesProximosModel.addAll(cupoesProximos);
    }

    private void setupGUI(Collection<Cartao> cartoes) {
        setSize(COMPRIMENTO, ALTURA);
        setTitle("App do cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox<Cartao> combo = new JComboBox<>(new Vector<>(cartoes));
        combo.setRenderer(new RendererCartoes());
        combo.addActionListener(l -> mudarCartao((Cartao) combo.getSelectedItem()));
        getContentPane().add(combo, BorderLayout.NORTH);

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setBorder(new TitledBorder("Dados do cartão"));

        JLabel lblTexto = new JLabel("Saldo:");
        lblTexto.setFont(ftTexto);
        panel.add(lblTexto);

        saldoLbl = new JLabel("0.00€");
        saldoLbl.setFont(ftTexto);
        panel.add(saldoLbl);

        layout.putConstraint(SpringLayout.NORTH, lblTexto, 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST,  lblTexto, 5, SpringLayout.WEST,  panel);
        layout.putConstraint(SpringLayout.NORTH, saldoLbl, 0, SpringLayout.NORTH, lblTexto);
        layout.putConstraint(SpringLayout.EAST,  saldoLbl, -5, SpringLayout.EAST, panel);

        // Cupões atuais
        JPanel atuais = new JPanel(new BorderLayout());
        atuais.setBorder(new TitledBorder("Cupões em vigor"));
        cupoesAtuaisModel = new DefaultListModel<>();
        JList<Cupao> listAtuais = new JList<>(cupoesAtuaisModel);
        listAtuais.setCellRenderer(new RendererCupoes());
        atuais.add(new JScrollPane(listAtuais));
        panel.add(atuais);

        layout.putConstraint(SpringLayout.NORTH, atuais, 3, SpringLayout.SOUTH, lblTexto);
        layout.putConstraint(SpringLayout.EAST,  atuais, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST,  atuais,  3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, atuais, ALTURA_ATUAIS, SpringLayout.NORTH, atuais);

        // Botão usar cartão
        JButton bt = new JButton("Usar cartão");
        bt.addActionListener(l -> ativarCartao(listAtuais.getSelectedValuesList()));
        panel.add(bt);
        layout.putConstraint(SpringLayout.NORTH, bt, 3, SpringLayout.SOUTH, atuais);
        layout.putConstraint(SpringLayout.EAST,  bt, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST,  bt,  3, SpringLayout.WEST, panel);

        // Próximos cupões
        JPanel proximos = new JPanel(new BorderLayout());
        proximos.setBorder(new TitledBorder("Próximos Cupões"));
        cupoesProximosModel = new DefaultListModel<>();
        JList<Cupao> listFuturos = new JList<>(cupoesProximosModel);
        listFuturos.setCellRenderer(new RendererCupoes());
        listFuturos.setEnabled(false);
        proximos.add(new JScrollPane(listFuturos));
        panel.add(proximos);

        layout.putConstraint(SpringLayout.NORTH, proximos, 3, SpringLayout.SOUTH, bt);
        layout.putConstraint(SpringLayout.EAST,  proximos, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST,  proximos,  3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, proximos, -3, SpringLayout.SOUTH, panel);

        getContentPane().add(panel, BorderLayout.CENTER);

        if (!cartoes.isEmpty()) {
            combo.setSelectedIndex(0);
            mudarCartao(combo.getItemAt(0));
        }
    }

    private final class RendererCupoes extends DefaultListCellRenderer {
        private static final Dimension DIM_BASE = new Dimension(200, 60);
        private Cupao cupao;

        @Override
        public Dimension getPreferredSize() {
            return DIM_BASE;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            cupao = (Cupao) value;
            return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharCupao(g, this, cupao);
        }

        private void paintDadosCupao(Graphics g, String resumo, float desconto,
                LocalDate inicio, LocalDate fim) {
            g.setColor(Color.ORANGE);
            g.fillOval(2, 6, 45, 45);
            g.drawRoundRect(2, 2, DIM_BASE.width - 4, DIM_BASE.height - 4, 6, 6);
            g.setColor(Color.BLACK);
            g.setFont(ftDesconto);
            String descontoStr = String.format("%02d%%", (int) (desconto * 100));
            g.drawString(descontoStr, 5, 35);
            g.setFont(ftTexto);
            g.drawString(resumo, 60, 20);
            g.setFont(ftValidade);
            String validade = String.format("%02d/%02d - %02d/%02d",
                    inicio.getDayOfMonth(), inicio.getMonthValue(),
                    fim.getDayOfMonth(), fim.getMonthValue());
            g.drawString(validade, 60, 50);
        }
    }

}