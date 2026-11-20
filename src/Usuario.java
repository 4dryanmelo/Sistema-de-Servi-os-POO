public abstract class Usuario implements Autenticavel {
    private String nome;
    private final Acesso acesso;

    protected Usuario(String nome, String login, String senha) {
        renomear(nome);
        acesso = new Acesso(login, senha);
    }

    public String getNome() { return nome; }
    public String getLogin() { return acesso.getLogin(); }
    public boolean acessoValido() { return acesso != null && acesso.valido(); }
    public abstract String getPerfil();

    public final void renomear(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        this.nome = nome.trim();
    }

    @Override
    public boolean autenticar(String senha) {
        return acesso.confere(senha);
    }

    @Override
    public String toString() {
        return nome + " | " + getPerfil() + " | " + getLogin();
    }
}
