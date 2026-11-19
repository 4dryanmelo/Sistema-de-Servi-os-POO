package model;

import exception.RegraNegocioException;
import java.util.UUID;

public abstract class Usuario {
	private String id;
	private String nome;
	private String email;
	private String senha;

	public Usuario(String nome, String email, String senha) throws RegraNegocioException {
		this.id= UUID.randomUUID().toString();//define o id ja no construtor
		setNome(nome);
		setEmail(email);
		setSenha(senha);
	}

	public abstract String getTipoUsuario();
	public abstract double calcularTaxaPlataforma(double valorBase);
	//////////////////////// id
	public String getId() {
		return id;
	}
	//////////////////////////nome
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) throws RegraNegocioException{
		if(nome==null || nome.isBlank()) {
			throw new RegraNegocioException("O nome não pode ser vazio");
		}
		this.nome=nome;
	}
	////////////////////////email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws RegraNegocioException{
		if(email==null || !email.contains("@")) {
			throw new RegraNegocioException("Email inválido");
		}this.email=email;
	}
	////////////////////////////senha
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha)throws RegraNegocioException {
		if(senha==null|| senha.length()<4) {
			throw new RegraNegocioException("A senha deve conter no mínimo 4 caracteres");
		}this.senha=senha;
	}
}
