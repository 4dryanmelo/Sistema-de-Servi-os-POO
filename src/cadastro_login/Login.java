package cadastro_login;

public class Login {
	private String email;
	private String senha;
	
	public Login(String email,String senha) {
		this.email=email;
		this.senha=senha;
	}
	
	public String logar() {
		CadLogDAO log= new CadLogDAO();
		if(log.logar(email, senha)!=null) {
			return "Logado";
		}return "Erro";
		
	}

}
