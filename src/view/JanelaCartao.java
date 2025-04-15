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
 * Janela que simula a aplicação do cliente onde estes podem ver os cupões
 * associados ao seu cartão e ativá-los.
 * 
 */
public class JanelaCartao extends JFrame {

    // constantes dos elementos visuais a usar na janela
    private static final int ALTURA = 600;
    private static final int COMPRIMENTO = RendererCupoes.DIM_BASE.width + 70;
    private static final int ALTURA_ATUAIS = 4 * ALTURA / 7;
    private static final Font ftDesconto = new Font("Arial", Font.BOLD, 20);
    private static final Font ftTexto = ftDesconto.deriveFont(Font.PLAIN, 14);
    private static final Font ftValidade = ftTexto.deriveFont(12f);

    // elementos visuais
    private JLabel saldoLbl;
    private DefaultListModel<Cupao> cupoesAtuaisModel;
    private DefaultListModel<Cupao> cupoesProximosModel;

    // o cartão que está atualmente a ser visualizado
    private Cartao cardAtual;

    /**
     * Cria uma JanelaCartão com uma coleção de cartões. Esta solução admite que,
     * durante a execução da aplicação, não serão adicionados novos cartões, o que é
     * limitativo. Esta solução foi adotada para simplificar os testes já que, numa
     * situação real, teriamos uma aplicação por cartão e não uma para todos os
     * cartões.
     * 
     * @param cartoes a coleção de cartões existente quando se arranca com a
     *                aplicação
     */
    public JanelaCartao(Collection<Cartao> cartoes) {
        setupGUI(cartoes);
    }

    /**
     * Método chamado pela janela quando o utilizador seleciona outro cartão
     * 
     * @param c o cartão a ser usado
     */
    public void mudarCartao(Cartao c) {
        cardAtual = c;
        // TODO é preciso atualizar os cupões
        c.atualizarCupoes();

        // TODO é preciso colocar a informação certa nas variáveis
        long saldo = 0;
        List<Cupao> cupoesAtuais = List.of();
        List<Cupao> cupoesFuturos = List.of();

        updateSaldo(saldo);
        updateCupoesAtuais(cupoesAtuais);
        updateCupoesProximos(cupoesFuturos);
    }

    /**
     * Método chamado pela janela sempre que pretende obter a informação de um cupão
     * para a desenhar
     * 
     * @param g              onde desenhar os dados
     * @param rendererCupoes quem vai desenhar os dados
     * @param cupao          o cupao do qual se pretendem os dados
     */
    public void desenharCupao(Graphics g, RendererCupoes rendererCupoes, Cupao cupao) {
        // TODO colocar os dados certos nas variáveis
        String resumo = "resumo";
        float desconto = 0.01f;
        LocalDate comeca = LocalDate.now();
        LocalDate acaba = LocalDate.now();

        // fazer o desenho da informação
        rendererCupoes.paintDadosCupao(g, resumo, desconto, comeca, acaba);
    }

    /**
     * Método chamado quando o utilizador pressiona o botão de ativar o cartão para
     * realizar uma compra, ativando também os cupões selecionados.
     * 
     * @param cupoesSelecionados lista com os cupões selecionados para ativar
     */
    private void ativarCartao(List<Cupao> cupoesSelecionados) {
        // TODO ativar o cartão com os cupões selecionados

        // mostrar a mensagem de que se está à espera de usar o cartão
        mostrarMensagem("Pode usar o cartão", "Cartão ativo");

        // no fim de mostrar a mensagem, atualizar a informação toda do cartão, pois
        // podem ter sido removidos cupões e o saldo também pode ter sofrido alterações
        mudarCartao(cardAtual);
    }

    /**
     * Rsponsável por desenhar a info do cartão na lista
     */
    private final class RendererCartoes extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Cartao c = (Cartao) value;

            // TODO colocar a informação certa na variável
            String numero = "1233";
            return super.getListCellRendererComponent(list, numero, index, isSelected, cellHasFocus);
        }
    }

    /**
     * Atualiza o saldo na janela
     * 
     * @param saldo valor do saldo a apresentar
     */
    private void updateSaldo(long saldo) {
        saldoLbl.setText(String.format("%.2f€", saldo / 100.0f));
    }

    /**
     * Atualiza a lista dos cupões em vigor hoje
     * 
     * @param cupoesAtuais a lista dos cupoes em vigor
     */
    private void updateCupoesAtuais(List<Cupao> cupoesAtuais) {
        cupoesAtuaisModel.clear();
        cupoesAtuaisModel.addAll(cupoesAtuais);
    }

    /**
     * Atualiza a lista dos cupões que estão em vigor no futuro
     * 
     * @param cupoesProximos os cupões que estarão em vigor no futuro
     */
    private void updateCupoesProximos(List<Cupao> cupoesProximos) {
        cupoesProximosModel.clear();
        cupoesProximosModel.addAll(cupoesProximos);
    }

    /**
     * Inicializa a interface gráfica da janela
     * 
     * @param cartoes a coleção de cartões que existem na empresa
     */
    private void setupGUI(Collection<Cartao> cartoes) {
        setSize(COMPRIMENTO, ALTURA);
        setTitle("App do cliente");
        JComboBox<Cartao> listaCartoes = new JComboBox<>(new Vector<>(cartoes));
        listaCartoes.setRenderer(new RendererCartoes());
        listaCartoes.addActionListener(l -> mudarCartao((Cartao) listaCartoes.getSelectedItem()));
        getContentPane().add(listaCartoes, BorderLayout.NORTH);

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
        panel.setBorder(new TitledBorder("Dados do cartão"));

        Font ftSaldo = new Font("Arial", Font.BOLD, 18);
        JLabel saldoTxtLbl = new JLabel("Saldo: ");
        saldoTxtLbl.setFont(ftSaldo);
        saldoLbl = new JLabel("0.00€");
        saldoLbl.setFont(ftSaldo);
        panel.add(saldoTxtLbl);
        panel.add(saldoLbl);

        layout.putConstraint(SpringLayout.NORTH, saldoTxtLbl, 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, saldoTxtLbl, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, saldoLbl, 0, SpringLayout.NORTH, saldoTxtLbl);
        layout.putConstraint(SpringLayout.EAST, saldoLbl, -5, SpringLayout.EAST, panel);

        JPanel atuais = new JPanel(new BorderLayout());
        atuais.setBorder(new TitledBorder("Cupões em vigor"));
        panel.add(atuais);
        cupoesAtuaisModel = new DefaultListModel<>();
        JList<Cupao> listaCupoes = new JList<>(cupoesAtuaisModel);
        listaCupoes.setCellRenderer(new RendererCupoes());
        atuais.add(new JScrollPane(listaCupoes));

        layout.putConstraint(SpringLayout.NORTH, atuais, 3, SpringLayout.SOUTH, saldoTxtLbl);
        layout.putConstraint(SpringLayout.EAST, atuais, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST, atuais, 3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, atuais, ALTURA_ATUAIS, SpringLayout.NORTH, atuais);

        JButton usarBt = new JButton("Usar cartão");
        usarBt.addActionListener(l -> ativarCartao(listaCupoes.getSelectedValuesList()));
        panel.add(usarBt);
        layout.putConstraint(SpringLayout.NORTH, usarBt, 3, SpringLayout.SOUTH, atuais);
        layout.putConstraint(SpringLayout.EAST, usarBt, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST, usarBt, 3, SpringLayout.WEST, panel);

        JPanel proximos = new JPanel(new BorderLayout());
        proximos.setBorder(new TitledBorder("Próximos Cupões"));
        panel.add(proximos);
        cupoesProximosModel = new DefaultListModel<>();
        JList<Cupao> listaProximosCupoes = new JList<>(cupoesProximosModel);
        listaProximosCupoes.setCellRenderer(new RendererCupoes());
        listaProximosCupoes.setEnabled(false);
        proximos.add(new JScrollPane(listaProximosCupoes));
        layout.putConstraint(SpringLayout.NORTH, proximos, 3, SpringLayout.SOUTH, usarBt);
        layout.putConstraint(SpringLayout.EAST, proximos, -3, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.WEST, proximos, 3, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.SOUTH, proximos, -3, SpringLayout.SOUTH, panel);

        getContentPane().add(panel, BorderLayout.CENTER);
        if (listaCartoes.getItemCount() > 0)
            listaCartoes.setSelectedIndex(0);
    }

    private final class RendererCupoes extends DefaultListCellRenderer {
        private static final Dimension DIM_BASE = new Dimension(200, 60);
        private Cupao cupao;

        @Override
        public Dimension getPreferredSize() {
            return DIM_BASE;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            cupao = (Cupao) value;
            return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharCupao(g, this, cupao);
        }

        private void paintDadosCupao(Graphics g, String resumo, float desconto, LocalDate inicio, LocalDate fim) {
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
            String validade = String.format("%02d/%02d - %02d/%02d", inicio.getDayOfMonth(), inicio.getMonthValue(),
                    fim.getDayOfMonth(), fim.getMonthValue());
            g.drawString(validade, 60, 50);
        }

    }

    /**
     * Mostra uma janela de diálogo com a mensagem e o título indicados e fica à
     * espera que o utilizador pressione em ok. Enquento espera, a janela de cartões
     * permanece bloqueada.
     * 
     * @param mensagem a mensagem a aparecer na janela de diálogo
     * @param titulo   o título da janela de diálogo.
     */
    private void mostrarMensagem(String mensagem, String titulo) {
        JDialog dialog = new JDialog(this, titulo, Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setLayout(new BorderLayout());

        JOptionPane optionPane = new JOptionPane(mensagem, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        optionPane.addPropertyChangeListener(e2 -> {
            String prop = e2.getPropertyName();
            if (dialog.isVisible() && (e2.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                dialog.dispose();
            }
        });
        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        final Timer t = new Timer(500, list -> {
            if (!cardAtual.estaAtivo()) {
                ((Timer) list.getSource()).stop();
                dialog.dispose();
            }
        });
        t.start();
        dialog.setVisible(true);
    }
}
