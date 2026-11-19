package model;

import exception.RegraNegocioException;

public class Cliente extends Usuario{
	private String cpfCnpj;
	
	public Cliente(String nome, String email, String senha, String empresa) throws RegraNegocioException {
		super(nome,email,senha);
		setCpfCnpj(cpfCnpj);
	}
	
	@Override
	public String getTipoUsuario() {
		return "CLIENTE";
	}

	@Override
	public double calcularTaxaPlataforma(double valorBase) {
		return valorBase*0.05;
	}
	
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) throws RegraNegocioException{
		if(cpfCnpj == null || cpfCnpj.isBlank()) {
			throw new RegraNegocioException("CPF ou CNPJ é obrigatório");
		}
		String numeros= cpfCnpj.replace(".","").replace("-","").replace("/", "");
		
		if(numeros.length()!=11 || numeros.length()!=14) {
			throw new RegraNegocioException("CPF ou CNPJ inválido");
		}this.cpfCnpj=cpfCnpj;
	}
}

	

