package ui;

import dao.UsuarioDAO;
import model.Cliente;
import model.Pedido;
import model.Prestador;
import model.Servico;
import model.Usuario;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class TelaServicos extends JFrame {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final ArrayList<Servico> servicos = new ArrayList<>();
    private final ArrayList<Pedido> pedidos = new ArrayList<>();

    public TelaServicos() {
        super("Sistema de serviços");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 340);
        setLocationRelativeTo(null);
        mostrarLogin();
    }

    private void mostrarLogin() {
        JTextField login = new JTextField(16);
        JPasswordField senha = new JPasswordField(16);
        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
        campos.add(new JLabel("Login"));
        campos.add(login);
        campos.add(new JLabel("Senha"));
        campos.add(senha);
        JButton entrar = botao("Entrar", () -> {
            Usuario usuario = dao.logar(login.getText(), new String(senha.getPassword()));
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Login ou senha incorretos.");
            } else {
                mostrarPedidos(usuario);
            }
        });
        campos.add(entrar);
        campos.add(botao("Criar conta", this::cadastrar));
        JPanel painel = new JPanel(new GridBagLayout());
        painel.add(campos);
        trocarTela(painel);
        getRootPane().setDefaultButton(entrar);
    }

    private void cadastrar() {
        JTextField nome = new JTextField();
        JTextField login = new JTextField();
        JPasswordField senha = new JPasswordField();
        JComboBox<String> perfil = new JComboBox<>(new String[]{"Cliente", "Prestador"});
        Object[] campos = {"Nome", nome, "Login", login, "Senha", senha, "Perfil", perfil};
        if (!confirmou("Criar conta", campos)) return;
        String segredo = new String(senha.getPassword());
        Usuario novo = perfil.getSelectedIndex() == 0
                ? new Cliente(nome.getText(), login.getText(), segredo)
                : new Prestador(nome.getText(), login.getText(), segredo);
        if (dao.cadastrar(novo)) mostrarPedidos(novo);
        else JOptionPane.showMessageDialog(this, "Este login já está cadastrado.");
    }

    private void mostrarPedidos(Usuario usuario) {
        boolean cliente = usuario instanceof Cliente;
        DefaultListModel<Pedido> modelo = new DefaultListModel<>();
        for (Pedido pedido : pedidos) {
            Usuario dono = cliente ? pedido.getCliente() : pedido.getServico().getPrestador();
            if (dono.getLogin().equals(usuario.getLogin())) modelo.addElement(pedido);
        }
        JList<Pedido> lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel acoes = new JPanel();
        if (cliente) {
            acoes.add(botao("Solicitar serviço", () -> solicitar((Cliente) usuario)));
            acoes.add(botao("Editar recado", () -> {
                Pedido pedido = pedidoSelecionado(lista);
                String texto = JOptionPane.showInputDialog(this, "Recado para o prestador", pedido.getRecado());
                if (texto != null) pedido.editarRecado(texto);
                lista.repaint();
            }));
            acoes.add(botao("Apagar pedido", () -> {
            	Pedido pedido = pedidoSelecionado(lista);
            	if(confirmou("Apagar pedido", new Object[] {"Deseja remover o pedido"+pedido.getId() + "?"})) {
            		pedidos.remove(pedido);
            		mostrarPedidos(usuario);
            	}
            }));
        } else {
            Prestador prestador = (Prestador) usuario;
            acoes.add(botao("Oferecer serviço", () -> formularioServico(prestador, null)));
            acoes.add(botao("Editar serviço", () -> escolherParaEditar(prestador)));
            acoes.add(botao("Concluir pedido", () -> {
                prestador.concluirPedido(pedidoSelecionado(lista));
                lista.repaint();
            }));
        }
        acoes.add(botao("Sair", this::mostrarLogin));
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        String titulo = cliente ? "Meus pedidos" : "Pedidos recebidos";
        painel.add(new JLabel(titulo + " — " + usuario.getNome()), BorderLayout.NORTH);
        if (modelo.isEmpty()) painel.add(new JLabel("Nenhum pedido ainda.", SwingConstants.CENTER));
        else painel.add(new JScrollPane(lista));
        painel.add(acoes, BorderLayout.SOUTH);
        trocarTela(painel);
        getRootPane().setDefaultButton(null);
    }

    private Pedido pedidoSelecionado(JList<Pedido> lista) {
        Pedido pedido = lista.getSelectedValue();
        if (pedido == null) throw new IllegalArgumentException("Selecione um pedido na lista.");
        return pedido;
    }

    private void formularioServico(Prestador prestador, Servico atual) {
        JTextField nome = new JTextField(atual == null ? "" : atual.getNome(), 20);
        JComboBox<String> categoria = new JComboBox<>(new String[]{"Físico", "Intelectual"});
        if (atual != null) categoria.setSelectedItem(atual.getCategoria());
        String titulo = atual == null ? "Oferecer serviço" : "Editar serviço";
        if (!confirmou(titulo, new Object[]{"Nome do serviço", nome, "Categoria", categoria})) return;
        String tipo = (String) categoria.getSelectedItem();
        if (atual == null) servicos.add(prestador.oferecerServico(servicos.size() + 1, nome.getText(), tipo));
        else atual.editar(nome.getText(), tipo);
        mostrarPedidos(prestador);
        JOptionPane.showMessageDialog(this, "Serviço salvo.");
    }

    private void escolherParaEditar(Prestador prestador) {
        ArrayList<Servico> meus = new ArrayList<>();
        for (Servico servico : servicos) {
            if (servico.getPrestador().getLogin().equals(prestador.getLogin())) meus.add(servico);
        }
        if (meus.isEmpty()) throw new IllegalStateException("Você ainda não ofereceu serviços.");
        Servico atual = (Servico) JOptionPane.showInputDialog(this, "Escolha seu serviço", "Editar serviço",
                JOptionPane.PLAIN_MESSAGE, null, meus.toArray(), meus.get(0));
        if (atual != null) formularioServico(prestador, atual);
    }

    private void solicitar(Cliente cliente) {
    if (servicos.isEmpty()) throw new IllegalStateException("Ainda não há serviços disponíveis.");
    JComboBox<Servico> opcoes = new JComboBox<>(servicos.toArray(new Servico[0]));
    JTextField recado = new JTextField();
    JTextField data = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    if (!confirmou("Solicitar serviço", new Object[]{"Serviço", opcoes, "Recado", recado, "Data (dd/MM/aaaa)", data})) return;
    Servico servico = (Servico) opcoes.getSelectedItem();
    pedidos.add(cliente.solicitarServico(pedidos.size() + 1, servico, recado.getText(), data.getText()));
    mostrarPedidos(cliente);
}

    private boolean confirmou(String titulo, Object[] campos) {
        return JOptionPane.showConfirmDialog(this, campos, titulo, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    private JButton botao(String texto, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> {
            try {
                acao.run();
            } catch (IllegalArgumentException | IllegalStateException erro) {
                JOptionPane.showMessageDialog(this, erro.getMessage());
            }
        });
        return botao;
    }

    private void trocarTela(JPanel painel) {
        setContentPane(painel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaServicos().setVisible(true));
    }
}
