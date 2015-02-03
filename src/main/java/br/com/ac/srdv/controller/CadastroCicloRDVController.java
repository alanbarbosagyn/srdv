package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.PhaseId;

import org.primefaces.event.SelectEvent;

import br.com.ac.srdv.dao.CicloRDVDao;
import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.KmLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.CicloRDV;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.Empresa;
import br.com.ac.srdv.modelo.Filial;
import br.com.ac.srdv.modelo.KmLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class CadastroCicloRDVController {

	private Usuario usuario;
	private Empresa empresa;
	private Filial filial;
	private List<Usuario> usuarios;
	private List<Ciclo> tipos;
	private Ciclo tipo;
	private int tipoId;

	private CicloRDV cicloRDV;
	private List<CicloRDV> listaCicloRDV;

	private boolean iniciado;

	public CadastroCicloRDVController() {
		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -370);
			Date dIni = c.getTime();

			tipos = new CicloDao().getTipos(dIni);
			tipo = new CicloDao().getTipo(new Date());
			tipoId = tipo.getId();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
			usuario = Sessao.getLogin().getUsuario();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO!", e.getMessage());
		}
		novo();
	}

	public String salvar() {

		cicloRDV.setUsuario(usuario.getLogin());
		cicloRDV.setTipo(tipo.getTipo());

		if (cicloRDV.getUsuario() == null) {
			Mensagem.Erro("ERRO AO SALVAR!", "ESCOLHA UM USUÁRIO");
			return "";
		}
		if (cicloRDV.getTipo() == null) {
			Mensagem.Erro("ERRO AO SALVAR!", "ESCOLHA UM RDV");
			return "";
		}
		if (cicloRDV.getData() == null) {
			Mensagem.Erro("ERRO AO SALVAR!", "ESCOLHA UMA DATA");
			return "";
		}
		if (cicloRDV.getStatus() == null) {
			Mensagem.Erro("ERRO AO SALVAR!", "ESCOLHA UM STATUS");
			return "";
		}

		try {
			new CicloRDVDao().salvar(cicloRDV);
			Mensagem.Info("RDV SALVO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO SALVAR RDV!", e.getMessage());
		}

		novo();
		return "";
	}

	public String novo() {

		cicloRDV = new CicloRDV();

		listaCicloRDV = new ArrayList<CicloRDV>();
		try {
			listaCicloRDV = new CicloRDVDao().getCicloRDVs(usuario.getLogin(),
					tipo.getTipo());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public String editar() {
		return "";
	}

	public String excluir() {
		try {
			new CicloRDVDao().excluir(cicloRDV);
			Mensagem.Info("CICLO RDV EXCLUÍDO COM SUCESSO!", "");
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO EXCLUIR RDV!", e.getMessage());
		}

		novo();
		return "";
	}

	public String Consultar() {

		// if (dataInicial == null || dataFinal == null) {
		// return "ConsultaDespesas";
		// }

		for (Ciclo t : tipos) {
			if (t.getId() == tipoId) {
				tipo = t;
				break;
			}
		}

		try {
			usuario = new UsuarioDao().getUsuario(usuario.getLogin());
			filial = new FilialDao().getFilial(usuario.getFilial());
			empresa = new EmpresaDao().getEmpresa(usuario.getEmpresa());
		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CARREGAR USUÁRIO!", e1.getMessage());
			e1.printStackTrace();
			return "";
		}

		novo();

		Date dataInicial = tipo.getDataInicial();
		Date dataFinal = tipo.getDataFinal();

		double despesaDinheiro = 0;
		double despesaCartao = 0;
		double despesaTotal = 0;
		double despesaKM = 0;
		double despesaKMCartao = 0;
		double kmReceber = 0;
		double kmTotal = 0;
		double diferencaKM = 0;
		double valorReceber = 0;

		List<DespesaLancamento> despesas = new ArrayList<DespesaLancamento>();
		List<KmLancamento> kms = new ArrayList<KmLancamento>();
		try {
			despesas = new DespesaLancamentoDao().getDespesasUsuario(
					usuario.getLogin(), dataInicial, dataFinal);
			kms = new KmLancamentoDao().getDespesasUsuario(usuario.getLogin(),
					dataInicial, dataFinal);

			for (DespesaLancamento d : despesas) {
				despesaTotal += d.getDespesaValor();
				if (d.getFormaPagamento().equals("DINHEIRO"))
					despesaDinheiro += d.getDespesaValor();
				if (d.getFormaPagamento().equals("CARTAO"))
					despesaCartao += d.getDespesaValor();
				if (d.getDespesa().equals("COMBUSTIVEL"))
					despesaKM += d.getDespesaValor();
				if (d.getDespesa().equals("COMBUSTIVEL")
						&& d.getFormaPagamento().equals("CARTAO"))
					despesaKMCartao += d.getDespesaValor();
			}

			for (KmLancamento km : kms) {
				kmTotal += km.getKm();
				kmReceber += km.getIndiceKm() * km.getKm();
			}

			diferencaKM = kmReceber - despesaKM;
			valorReceber = despesaDinheiro + diferencaKM;

		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR DESPESAS", e.getMessage());
			e.printStackTrace();
			return "";
		}

		iniciado = true;
		return "";
	}

	public String novaConsulta() {

		iniciado = false;
		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		usuario = Sessao.getLogin().getUsuario();

		return "CadastroCicloRDV";
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
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Ciclo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Ciclo> tipos) {
		this.tipos = tipos;
	}

	public Ciclo getTipo() {
		return tipo;
	}

	public void setTipo(Ciclo tipo) {
		this.tipo = tipo;
	}

	public int getTipoId() {
		return tipoId;
	}

	public void setTipoId(int tipoId) {
		this.tipoId = tipoId;
	}

	public CicloRDV getCicloRDV() {
		return cicloRDV;
	}

	public void setCicloRDV(CicloRDV cicloRDV) {
		this.cicloRDV = cicloRDV;
	}

	public List<CicloRDV> getListaCicloRDV() {
		return listaCicloRDV;
	}

	public void setListaCicloRDV(List<CicloRDV> listaCicloRDV) {
		this.listaCicloRDV = listaCicloRDV;
	}

	public boolean isIniciado() {
		return iniciado;
	}

	public void setIniciado(boolean iniciado) {
		this.iniciado = iniciado;
	}

}
