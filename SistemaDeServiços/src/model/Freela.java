package model;

import exception.RegraNegocioException;

public class Freela extends Usuario {
	private String especialidade;
	private double valorHora;

	public Freela(String nome, String email, String senha,String especialidade, double valorHora) throws RegraNegocioException {
		super(nome, email, senha);
		this.especialidade=especialidade;
		this.valorHora=valorHora;
	}

	@Override
	public String getTipoUsuario() {
		return "FREELA";
	}

	@Override
	public double calcularTaxaPlataforma(double valorBase) {
		return valorBase*0.1;
	}
	
	public String getEspecialidade() {
		return especialidade;
	}
	public double getValorHora() {
		return valorHora;
	}
}
