package br.com.ac.srdv.modelo;

import java.util.Date;

public class Tipo {

	private int id;
	private String tipo;
	private Date dataInicial;
	private Date dataFinal;
	private boolean liberado;

	public int getId() {
		return id;
	}

	public String getTipo() {
		return tipo;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}

}
