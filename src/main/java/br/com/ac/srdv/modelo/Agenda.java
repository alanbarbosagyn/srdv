package br.com.ac.srdv.modelo;

import java.util.Date;

public class Agenda {

	private int id;
	private String usuario;
	private Date data;
	private String local;
	private String acaoPrevista;
	private String acaoRealizada;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getAcaoPrevista() {
		return acaoPrevista;
	}

	public void setAcaoPrevista(String acaoPrevista) {
		this.acaoPrevista = acaoPrevista;
	}

	public String getAcaoRealizada() {
		return acaoRealizada;
	}

	public void setAcaoRealizada(String acaoRealizada) {
		this.acaoRealizada = acaoRealizada;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
