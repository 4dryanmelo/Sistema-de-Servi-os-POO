package cadastro_login;

public class Cadastro {
	private String nome;
	private String email;
	private String senha;
	
	public Cadastro (String nome,String email,String senha) {
		this.nome=nome;
		this.email=email;
		this.senha=senha;
	}
	
	public String cadastrar() {
		CadLogDAO cad= new CadLogDAO();
		if(!cad.cadastrar(this)){
			return "Já possui cadastro";
		}return "Cadastrado";
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}
	
	
}
