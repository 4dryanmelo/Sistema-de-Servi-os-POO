public final class Cliente extends Usuario {
    public Cliente(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getPerfil() { return "Cliente"; }


    }
