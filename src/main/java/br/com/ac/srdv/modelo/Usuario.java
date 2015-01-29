package br.com.ac.srdv.modelo;

public class Usuario {

	private int id;
	private String login;
	private String nome;
	private String senha;
	private int empresa;
	private int filial;
	private int cargo;
	private boolean admin;
	private boolean ativo;
	private String centroCusto;
	private String telefone;
	private String cartaoCredito;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getEmpresa() {
		return empresa;
	}

	public int getFilial() {
		return filial;
	}

	public int getCargo() {
		return cargo;
	}

	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}

	public void setFilial(int filial) {
		this.filial = filial;
	}

	public void setCargo(int cargo) {
		this.cargo = cargo;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(String cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

}
