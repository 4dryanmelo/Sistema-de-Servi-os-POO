public final class Servico {
    private final int id;
    private final Prestador prestador;
    private String nome;
    private String categoria;

    public Servico(int id, Prestador prestador, String nome, String categoria) {
        if (id < 1 || prestador == null) {
            throw new IllegalArgumentException("O serviço precisa de um ID positivo e de um prestador.");
        }
        this.id = id;
        this.prestador = prestador;
        editar(nome, categoria);
    }

    public int getId() { return id; }
    public Prestador getPrestador() { return prestador; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }

    public void editar(String nome, String categoria) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do serviço.");
        }
        if (!"Físico".equals(categoria) && !"Intelectual".equals(categoria)) {
            throw new IllegalArgumentException("Escolha a categoria Físico ou Intelectual.");
        }
        this.nome = nome.trim();
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "#" + id + " " + nome + " | " + categoria + " | " + prestador.getNome();
    }
}
