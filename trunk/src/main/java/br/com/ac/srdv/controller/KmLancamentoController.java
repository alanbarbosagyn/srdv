package br.com.ac.srdv.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.SelectEvent;

import br.com.ac.srdv.dao.CargoDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.KmLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Filial;
import br.com.ac.srdv.modelo.KmLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class KmLancamentoController {

	private KmLancamento kmLancamento;
	private List<Usuario> usuarios;

	public KmLancamentoController() {
		novo();
	}

	public void handleDateSelect(SelectEvent event) {

		/**
		 * ESSE TRECHO DE CÃ“DIGO SERVE PARA ATUALIZAR. FAZER O JSF CHAMAR OS
		 * MÃ‰TODOS SETS ANTES DO MÃ‰TODO ATUALIZAR A PÃ�GINA
		 **/
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		}

		System.out.println("BUSCANDO O RDV PELA DATA");

		if (kmLancamento.getData() == null)
			return;

		CicloDao tipDao = new CicloDao();
		Ciclo tipo = null;
		try {
			tipo = tipDao.getTipo(kmLancamento.getData());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO ACESSAR O BANCO DE DADOS", e.getMessage());
		}

		if (tipo == null) {
			Mensagem.Erro(
					"NENHUM RDV FOI ENCONTRADO COM ESSA DATA '"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(kmLancamento.getData()),
					"DIGITE A DATA");
			kmLancamento.setTipo(null);
			kmLancamento.setData(null);
		} else {
			kmLancamento.setTipo(tipo.getTipo());
		}
	}

	public void formatarValor(ValueChangeEvent event) {
		/**
		 * ESSE TRECHO DE CÃ“DIGO SERVE PARA ATUALIZAR. FAZER O JSF CHAMAR OS
		 * MÃ‰TODOS SETS ANTES DO MÃ‰TODO ATUALIZAR A PÃ�GINA
		 **/
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		}
	}

	public String novo() {
		kmLancamento = new KmLancamento();

		kmLancamento.setUsuario(Sessao.getLogin().getUsuario().getLogin());

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO!", e.getMessage());
		}

		return "KMLancamento";
	}

	public void salvar(ActionEvent event) {

		if (tudoOK() == false)
			return;

		CargoDao carDao = new CargoDao();
		EmpresaDao empDao = new EmpresaDao();
		FilialDao filDao = new FilialDao();

		Usuario u;
		try {
			u = new UsuarioDao().getUsuario(kmLancamento.getUsuario());
		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO", e1.getMessage());
			e1.printStackTrace();
			return;
		}
		kmLancamento.setNome(u.getNome());

		try {
			kmLancamento.setCargo(carDao.getCargo(u.getCargo()).getCargo());
			kmLancamento.setEmpresa(empDao.getEmpresa(u.getEmpresa())
					.getEmpresa());
			kmLancamento.setCentroCusto(u.getCentroCusto());

			Filial filAux = filDao.getFilial(u.getFilial());
			kmLancamento.setFilial(filAux.getFilial());
			kmLancamento.setIndiceKm(filAux.getIndiceKm());

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"ERRO AO ACESSAR O BANCO DE DADOS", e
											.getMessage()));
			return;
		}

		kmLancamento
				.setLocalizacao(kmLancamento.getLocalizacao().toUpperCase());

		KmLancamentoDao desDao = new KmLancamentoDao();
		try {
			desDao.salvar(kmLancamento);
			Mensagem.Info("DESPESA SALVA COM SUCESSO!", "");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"ERRO AO SALVAR", e.getMessage()));
			return;
		}

		novo();
	}

	public void limpar(ActionEvent event) {
		novo();
	}

	public boolean tudoOK() {
		boolean retorno = true;
		;
		if (kmLancamento.getTipo() == null || kmLancamento.getTipo().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"TIPO NÃO DIGITADO.", "ESCOLHA O TIPO!"));
			retorno = false;
		}

		if (kmLancamento.getKm() <= 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"KM NÃO DIGITADO.", "DIGITE O KM!"));
			retorno = false;
		}
		if (kmLancamento.getData() == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"DATA NÃO DIGITADO.", "ESCOLHA A DATA!"));
			retorno = false;
		}

		// VERIFICA SE ESTÁ NO RDV ATUAL

		try {
			Ciclo t = new CicloDao().getTipo(kmLancamento.getData());

			if (t.isLiberado() == false) {
				Mensagem.Erro(
						"O RDV SELECIONADO ESTÁ BLOQUEADO PARA LANÇAMENTOS!",
						"");
				retorno = false;
			}
		} catch (Exception e) {
			Mensagem.Erro("ERRO VALIDAR INFORMAÇÕES", e.getMessage());
			e.printStackTrace();
		}

		// VERIFICA SE O USUÁRIO JÁ FEZ ALGUM LANÇAMENTO NO DIA
		try {
			List<KmLancamento> lancamentos = new KmLancamentoDao()
					.getDespesasUsuario(kmLancamento.getUsuario(),
							kmLancamento.getData(), kmLancamento.getData());
			if (lancamentos != null && lancamentos.size() > 0) {
				Mensagem.Erro(
						"JÁ EXISTE UM LANÇAMENTO DE KM PARA A DATA INFORMADA",
						"");
				retorno = false;
			}
		} catch (Exception e) {
			Mensagem.Erro("ERRO VALIDAR INFORMAÇÕES", e.getMessage());
			e.printStackTrace();
		}

		// try {
		//
		// // SUBTRAI 10 DIAS
		// Calendar calendarData = Calendar.getInstance();
		// calendarData.setTime(new Date());
		// calendarData.add(Calendar.DATE, 10);
		// Date dataLimite = calendarData.getTime();
		//
		// Tipo rdv = new TipoDao().getTipo(new Date());
		// Tipo rdv10 = new TipoDao().getTipo(dataLimite);
		//
		// if (rdv == null
		// || kmLancamento.getTipo().equals(rdv.getTipo()) == false)
		// if (rdv10 == null
		// || kmLancamento.getTipo().equals(rdv10.getTipo()) == false) {
		// Mensagem.Erro(
		// "VOCÊ ESTÁ TENTANDO LANÇAR DESPESAS EM UM RDV NÃO PERMITIDO!",
		// "");
		// retorno = false;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// Mensagem.Erro("ERRO AO CONSULTAR O RDV ATUAL!", e.getMessage());
		// retorno = false;
		// }

		return retorno;

	}

	public String editar() {
		// novo();
		return "KMLancamento";
	}

	// ///////////////////////////////////////////////////////////////////////
	public KmLancamento getKmLancamento() {
		return kmLancamento;
	}

	public void setKmLancamento(KmLancamento kmLancamento) {
		this.kmLancamento = kmLancamento;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
