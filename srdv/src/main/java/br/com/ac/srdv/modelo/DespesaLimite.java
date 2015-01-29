package br.com.ac.srdv.modelo;

public class DespesaLimite {

	private int tipo;
	private String usuario;
	private double limite;
	private double limiteOriginal;

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite = limite;
	}

	public double getLimiteOriginal() {
		return limiteOriginal;
	}

	public void setLimiteOriginal(double limiteOriginal) {
		this.limiteOriginal = limiteOriginal;
	}

}
