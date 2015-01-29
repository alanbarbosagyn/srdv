package br.com.ac.srdv.controller;

import java.util.Date;
import java.util.List;

import br.com.ac.srdv.dao.TipoDao;
import br.com.ac.srdv.modelo.Tipo;
import br.com.ac.srdv.util.Mensagem;

public class CadastroRDVController {

	private List<Tipo> listaRdv;
	private Tipo rdv;
	private Date dataInicial;
	private Date dataFinal;

	public CadastroRDVController() {
		novo();
	}

	public String salvar() {

		if (rdv.getDataInicial() == null)
			return "";
		if (rdv.getDataFinal() == null)
			return "";
		if (rdv.getTipo() == null || rdv.getTipo().equals("")) {
			return "";
		}

		try {
			new TipoDao().salvar(rdv);
			Mensagem.Info("RDV SALVO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO SALVAR RDV!", e.getMessage());
		}

		novo();

		return "";
	}

	public String novo() {
		rdv = new Tipo();
		rdv.setLiberado(true);
		consultar();
		return "CadastroRDV";
	}

	public String consultar() {
		try {
			if (dataInicial == null || dataFinal == null) {
				listaRdv = new TipoDao().getTodosTipos();
			} else {
				listaRdv = new TipoDao().getTipos(dataInicial, dataFinal);
			}
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CONSULTAR RDV", e.getMessage());
		}
		return "";
	}

	public String editar() {
		return "";
	}

	public String excluir() {
		if (rdv.getId() == 0)
			return "";

		try {
			new TipoDao().excluir(rdv.getId());
			Mensagem.Info("RDV EXCLU�DO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO EXCLUIR RDV!", e.getMessage());
		}
		return "";
	}

	public List<Tipo> getListaRdv() {
		return listaRdv;
	}

	public void setListaRdv(List<Tipo> listaRdv) {
		this.listaRdv = listaRdv;
	}

	public Tipo getRdv() {
		return rdv;
	}

	public void setRdv(Tipo rdv) {
		this.rdv = rdv;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
