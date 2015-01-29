package br.com.ac.srdv.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.primefaces.event.SelectEvent;

import br.com.ac.srdv.dao.CargoDao;
import br.com.ac.srdv.dao.DespesaDao;
import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.DespesaLimiteDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.FormaPagamentoDao;
import br.com.ac.srdv.dao.TipoDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Despesa;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.DespesaLimite;
import br.com.ac.srdv.modelo.FormaPagamento;
import br.com.ac.srdv.modelo.Tipo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class DespesaLancamentoController {

	private DespesaLancamento despesaLancamento;
	private List<Usuario> usuarios;
	private List<Despesa> despesas;
	private List<FormaPagamento> formaPagamentos;
	Tipo tipo = null;

	public DespesaLancamentoController() {
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

		if (despesaLancamento.getData() == null)
			return;

		TipoDao tipDao = new TipoDao();

		try {
			tipo = tipDao.getTipo(despesaLancamento.getData());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO ACESSAR O BANCO DE DADOS", e.getMessage());
		}

		if (tipo == null) {
			Mensagem.Erro(
					"NENHUM RDV FOI ENCONTRADO COM ESSA DATA '"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(despesaLancamento.getData()),
					"DIGITE A DATA");
			despesaLancamento.setTipo(null);
			despesaLancamento.setData(null);
		} else {
			despesaLancamento.setTipo(tipo.getTipo());
		}

	}

	// public void formatarValor(ValueChangeEvent event) {
	//
	// /**
	// * ESSE TRECHO DE CÃ“DIGO SERVE PARA ATUALIZAR. FAZER O JSF CHAMAR OS
	// * MÃ‰TODOS SETS ANTES DO MÃ‰TODO ATUALIZAR A PÃ�GINA
	// **/
	// PhaseId phaseId = event.getPhaseId();
	// if (phaseId.equals(PhaseId.ANY_PHASE)) {
	// event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
	// event.queue();
	// }
	// }

	public String novo() {
		despesaLancamento = new DespesaLancamento();

		Usuario usuario = Sessao.getLogin().getUsuario();
		despesaLancamento.setUsuario(usuario.getLogin());

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO!", e.getMessage());
		}

		DespesaDao desDao = new DespesaDao();
		try {
			despesas = desDao.getTodasDespesas();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.Erro("ERRO AO ACESSAR O BANCO DE DADOS", e.getMessage());
		}
		try {
			formaPagamentos = new FormaPagamentoDao().getTodasFormaPagamentos();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.Erro("ERRO AO ACESSAR O BANCO DE DADOS", e.getMessage());
		}

		return "DespesaLancamento";
	}

	public void salvar(ActionEvent event) {

		if (tudoOK() == false)
			return;

		CargoDao carDao = new CargoDao();
		EmpresaDao empDao = new EmpresaDao();
		FilialDao filDao = new FilialDao();

		Usuario usuario = null;
		try {
			usuario = new UsuarioDao().getUsuario(despesaLancamento
					.getUsuario());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO", e.getMessage());
			e.printStackTrace();
			return;
		}
		despesaLancamento.setNome(usuario.getNome());
		try {
			despesaLancamento.setCargo(carDao.getCargo(usuario.getCargo())
					.getCargo());
			despesaLancamento.setEmpresa(empDao
					.getEmpresa(usuario.getEmpresa()).getEmpresa());
			despesaLancamento.setFilial(filDao.getFilial(usuario.getFilial())
					.getFilial());
			despesaLancamento.setCentroCusto(usuario.getCentroCusto());
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

		despesaLancamento.setObs(despesaLancamento.getObs().toUpperCase());

		try {
			tipo = new TipoDao().getTipo(despesaLancamento.getData());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO ACESSAR O BANCO DE DADOS", e.getMessage());
		}

		// ANTES DE SALVAR A DESPESA VALIDAR SE O USUÁRIO ESTÁ EXCEDENDO O
		// LIMITE DE CRÉDITO PARA O RDV
		try {
			DespesaLimite limite = new DespesaLimiteDao().getDespesaLimite(
					tipo.getId(), despesaLancamento.getUsuario());

			if (limite != null) {

				List<DespesaLancamento> despesas = new DespesaLancamentoDao()
						.getDespesasUsuario(despesaLancamento.getUsuario(),
								tipo.getDataInicial(), tipo.getDataFinal());

				double consumo = 0;
				for (int i = 0; i < despesas.size(); i++)
					consumo += despesas.get(i).getDespesaValor();

				if ((consumo + despesaLancamento.getDespesaValor()) > limite
						.getLimite()) {

					double creditoDisponivel = (limite.getLimite() - consumo);

					Mensagem.Erro("VOCÊ EXCEDEU O CRÉDITO DISPONÍVEL, ",
							new DecimalFormat("0.00").format(creditoDisponivel));

					return;
				}
			}

		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CARREGAR LIMITE!", e1.getMessage());
			e1.printStackTrace();
			return;
		}

		DespesaLancamentoDao desDao = new DespesaLancamentoDao();
		try {
			desDao.salvar(despesaLancamento);
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

		if (despesaLancamento.getTipo() == null
				|| despesaLancamento.getTipo().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"TIPO NÃO DIGITADO.", "ESCOLHA O TIPO!"));
			retorno = false;
		}
		if (despesaLancamento.getDespesa() == null
				|| despesaLancamento.getDespesa().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"DESPESA NÃO DIGITADO.", "ESCOLHA A DESPESA!"));
			retorno = false;
		}

		if (despesaLancamento.getFormaPagamento() == null
				|| despesaLancamento.getFormaPagamento().equals("")) {
			Mensagem.Erro("FORMA DE PAGAMENTO NÃO DIGITADO!",
					"DIGITE A FORMA DE PAGAMENTO");
			retorno = false;
		}

		if (despesaLancamento.getDespesaValor() <= 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"VALOR DA DESPESA NÃO DIGITADO.",
							"ESCOLHA O VALOR DA DESPESA!"));
			retorno = false;
		}
		if (despesaLancamento.getData() == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"DATA NÃO DIGITADO.", "ESCOLHA A DATA!"));
			retorno = false;
		}

		// VERIFICA SE ESTÁ NO RDV ATUAL

		try {
			Tipo t = new TipoDao().getTipo(despesaLancamento.getData());

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

		// try {
		//
		// // SUBTRAI 10 DIAS
		// Calendar calendarData = Calendar.getInstance();
		// calendarData.setTime(new Date());
		// calendarData.add(Calendar.DATE, -10);
		// Date dataLimite = calendarData.getTime();
		//
		// Tipo rdv = new TipoDao().getTipo(new Date());
		// Tipo rdv10 = new TipoDao().getTipo(dataLimite);
		//
		// if (rdv == null
		// || despesaLancamento.getTipo().equals(rdv.getTipo()) == false)
		// if (rdv10 == null
		// || despesaLancamento.getTipo().equals(rdv10.getTipo()) == false) {
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
		return "DespesaLancamento";
	}

	// ///////////////////////////////////////////////////////////////////////
	public DespesaLancamento getDespesaLancamento() {
		return despesaLancamento;
	}

	public List<Despesa> getDespesas() {
		return despesas;
	}

	public void setDespesaLancamento(DespesaLancamento despesaLancamento) {
		this.despesaLancamento = despesaLancamento;
	}

	public void setDespesas(List<Despesa> despesas) {
		this.despesas = despesas;
	}

	public List<FormaPagamento> getFormaPagamentos() {
		return formaPagamentos;
	}

	public void setFormaPagamentos(List<FormaPagamento> formaPagamentos) {
		this.formaPagamentos = formaPagamentos;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
