public final class Prestador extends Usuario {
    public Prestador(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getPerfil() { return "Prestador"; }

    }

