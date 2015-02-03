package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.util.Sessao;

public class ConsultaDespesasController {

	private List<Ciclo> tipos;
	private Ciclo tipoSelecionado;
	private int tipoId;
	private Date dataInicial;
	private Date dataFinal;
	private DespesaLancamento despesa;
	private List<DespesaLancamento> despesas;

	public ConsultaDespesasController() {
		try {

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -370);
			Date dIni = c.getTime();

			tipos = new CicloDao().getTipos(dIni);
			tipoId = new CicloDao().getTipo(new Date()).getId();

		} catch (Exception e) {
			e.printStackTrace();
		}
		dataFinal = new Date();
		despesas = new ArrayList<DespesaLancamento>();
	}

	public String Consultar() {

		// if (dataInicial == null || dataFinal == null) {
		// return "ConsultaDespesas";
		// }

		for (Ciclo t : tipos) {
			if (t.getId() == tipoId) {
				tipoSelecionado = t;
				break;
			}
		}

		dataInicial = tipoSelecionado.getDataInicial();
		dataFinal = tipoSelecionado.getDataFinal();

		LoginController usuario = Sessao.getLogin();

		DespesaLancamentoDao desDao = new DespesaLancamentoDao();
		try {
			despesas = desDao.getDespesasUsuario(usuario.getUsuario()
					.getLogin(), dataInicial, dataFinal);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERRO AO ACESSAR O BANCO DE DADOS", e
											.getMessage()));
		}

		return "ConsultaDespesas";
	}

	public String excluir() {

		DespesaLancamentoDao desDao = new DespesaLancamentoDao();
		try {
			desDao.excluir(despesa);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERRO AO ACESSAR O BANCO DE DADOS", e
											.getMessage()));
		}

		return Consultar();
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<DespesaLancamento> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaLancamento> despesas) {
		this.despesas = despesas;
	}

	public DespesaLancamento getDespesa() {
		return despesa;
	}

	public void setDespesa(DespesaLancamento despesa) {
		this.despesa = despesa;
	}

	public double getTotalDespesas() {

		if (despesas == null || despesas.size() == 0)
			return 0;

		double total = 0;
		for (DespesaLancamento d : despesas)
			total += d.getDespesaValor();
		return total;
	}

	public List<Ciclo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Ciclo> tipos) {
		this.tipos = tipos;
	}

	public Ciclo getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(Ciclo tipoSelecionado) {
		this.tipoSelecionado = tipoSelecionado;
	}

	public int getTipoId() {
		return tipoId;
	}

	public void setTipoId(int tipoId) {
		this.tipoId = tipoId;
	}
}
