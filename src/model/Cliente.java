package model;

public final class Cliente extends Usuario {
    public Cliente(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getPerfil() { return "Cliente"; }

    public Pedido solicitarServico(int id, Servico servico, String recado, String data) {
        return new Pedido(id, this, servico, recado, data);
    }
}
