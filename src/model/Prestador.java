package model;

public final class Prestador extends Usuario {
    public Prestador(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getPerfil() { return "Prestador"; }

    public Servico oferecerServico(int id, String nome, String categoria) {
        return new Servico(id, this, nome, categoria);
    }

    public void concluirPedido(Pedido pedido) {
        if (pedido == null || !getLogin().equals(pedido.getServico().getPrestador().getLogin())) {
            throw new IllegalArgumentException("Selecione um pedido de um dos seus serviços.");
        }
        pedido.concluir();
    }
}
