package br.com.ac.srdv.modelo;

public class Filial {

	private int id;
	private String filial;
	private String descricao;
	private double indiceKm;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getFilial() {
		return filial;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public double getIndiceKm() {
		return indiceKm;
	}

	public void setIndiceKm(double indiceKm) {
		this.indiceKm = indiceKm;
	}

}
