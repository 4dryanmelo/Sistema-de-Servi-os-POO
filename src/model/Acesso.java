package model;

public final class Acesso {
	private final String login;
	private final String senha;

	public Acesso(String login, String senha) {
		if (login == null || login.trim().isEmpty()) {
			throw new IllegalArgumentException("Informe o login.");
		}
		if (senha == null || senha.trim().isEmpty()) {
			throw new IllegalArgumentException("Informe a senha.");
		}
		this.login = login.trim().toLowerCase();
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

//	public boolean valido() {
//		return login != null && !login.trim().isEmpty() && senha != null && !senha.trim().isEmpty();
//	}nunca retorna false pq o construtor ja bloqueia login e senha null

	public boolean confere(String tentativa) {
		return senha.equals(tentativa);
	}
}
