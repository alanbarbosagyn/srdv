package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.dao.CargoDao;
import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.EmpresaDao;
import br.com.ac.srdv.dao.FilialDao;
import br.com.ac.srdv.dao.KmLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.Cargo;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.Empresa;
import br.com.ac.srdv.modelo.Filial;
import br.com.ac.srdv.modelo.KmLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;
import br.com.ac.srdv.util.Sessao;

public class ResumoRDVController {

	private Usuario usuario;
	private Empresa empresa;
	private Filial filial;
	private Cargo cargo;
	private List<Usuario> usuarios;
	private List<Ciclo> tipos;
	private Ciclo tipoSelecionado;
	private int tipoId;
	private Date dataInicial;
	private Date dataFinal;
	private List<DespesaLancamento> despesas;
	private List<KmLancamento> kms;

	private double despesaDinheiro = 0;
	private double despesaCartao = 0;
	private double despesaTotal = 0;
	private double despesaKM = 0;
	private double despesaKMCartao = 0;
	private double kmReceber = 0;
	private double kmTotal = 0;
	private double diferencaKM = 0;
	private double valorReceber = 0;

	public ResumoRDVController() {
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
		kms = new ArrayList<KmLancamento>();

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
			usuario = Sessao.getLogin().getUsuario();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIO!", e.getMessage());
		}

	}

	public String Consultar() {

		for (Ciclo t : tipos) {
			if (t.getId() == tipoId) {
				tipoSelecionado = t;
				break;
			}
		}

		try {
			usuario = new UsuarioDao().getUsuario(usuario.getLogin());
			filial = new FilialDao().getFilial(usuario.getFilial());
			empresa = new EmpresaDao().getEmpresa(usuario.getEmpresa());
			cargo = new CargoDao().getCargo(usuario.getCargo());
		} catch (Exception e1) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIO!", e1.getMessage());
			e1.printStackTrace();
			return "";
		}

		dataInicial = tipoSelecionado.getDataInicial();
		dataFinal = tipoSelecionado.getDataFinal();

		despesaDinheiro = 0;
		despesaCartao = 0;
		despesaTotal = 0;
		despesaKM = 0;
		despesaKMCartao = 0;
		kmReceber = 0;
		kmTotal = 0;
		diferencaKM = 0;
		valorReceber = 0;

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

		return "";
	}

	public String novaConsulta() {

		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		usuario = Sessao.getLogin().getUsuario();

		return "ResumoRDV";
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

	public List<DespesaLancamento> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaLancamento> despesas) {
		this.despesas = despesas;
	}

	public List<KmLancamento> getKms() {
		return kms;
	}

	public void setKms(List<KmLancamento> kms) {
		this.kms = kms;
	}

	public double getDespesaDinheiro() {
		return despesaDinheiro;
	}

	public void setDespesaDinheiro(double despesaDinheiro) {
		this.despesaDinheiro = despesaDinheiro;
	}

	public double getDespesaTotal() {
		return despesaTotal;
	}

	public void setDespesaTotal(double despesaTotal) {
		this.despesaTotal = despesaTotal;
	}

	public double getDespesaKM() {
		return despesaKM;
	}

	public void setDespesaKM(double despesaKM) {
		this.despesaKM = despesaKM;
	}

	public double getKmReceber() {
		return kmReceber;
	}

	public void setKmReceber(double kmReceber) {
		this.kmReceber = kmReceber;
	}

	public double getValorReceber() {
		return valorReceber;
	}

	public void setValorReceber(double valorReceber) {
		this.valorReceber = valorReceber;
	}

	public double getDespesaKMCartao() {
		return despesaKMCartao;
	}

	public void setDespesaKMCartao(double despesaKMCartao) {
		this.despesaKMCartao = despesaKMCartao;
	}

	public double getDiferencaKM() {
		return diferencaKM;
	}

	public void setDiferencaKM(double diferencaKM) {
		this.diferencaKM = diferencaKM;
	}

	public double getKmTotal() {
		return kmTotal;
	}

	public void setKmTotal(double kmTotal) {
		this.kmTotal = kmTotal;
	}

	public double getDespesaCartao() {
		return despesaCartao;
	}

	public void setDespesaCartao(double despesaCartao) {
		this.despesaCartao = despesaCartao;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

}
