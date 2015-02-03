package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.ac.srdv.dao.DespesaLancamentoDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.DespesaLancamento;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;

public class RelatorioDespesasResumidoController {

	private List<Ciclo> tipos;
	private Ciclo tipoSelecionado;
	private int tipoId;
	private Date dataInicial;
	private Date dataFinal;
	private List<Wrapper> despesas;

	public RelatorioDespesasResumidoController() {
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
		despesas = new ArrayList<Wrapper>();
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

		// Login usuario = Sessao.getLogin();

		// CARREGA TODOS OS USU�RIOS ATIVOS
		List<Usuario> usuarios;
		try {
			usuarios = new UsuarioDao().getTodosUsuariosAtivos();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR USU�RIOS", e.getMessage());
			e.printStackTrace();
			return "";
		}

		try {
			for (Usuario u : usuarios) {
				List<DespesaLancamento> desp = new DespesaLancamentoDao()
						.getDespesasUsuario(u.getLogin(), dataInicial,
								dataFinal);
				Wrapper w = new Wrapper();

				w.login = u.getLogin();
				w.usuarioNome = u.getNome();
				w.centroCusto = u.getCentroCusto();
				w.rdv = tipoSelecionado.getTipo();

				double despesaDinheiro = 0;
				double despesaTotal = 0;
				double despesaKM = 0;
				double valorReceber = 0;

				for (DespesaLancamento d : desp) {
					despesaTotal += d.getDespesaValor();
					if (d.getFormaPagamento().equals("DINHEIRO"))
						despesaDinheiro += d.getDespesaValor();
					if (d.getDespesa().equals("COMBUSTIVEL"))
						despesaKM += d.getDespesaValor();
				}
			}
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR DESPESAS", e.getMessage());
			e.printStackTrace();
			return "";
		}

		return "ConsultaDespesas";
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

	public class Wrapper {
		public String login;
		public String usuarioNome;
		public String centroCusto;
		public String rdv;
		public double despesaTotal;
		public double despesaDinheiro;
		public double despesaKM;
		public double KMReceber;
		public double valorReceber;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getUsuarioNome() {
			return usuarioNome;
		}

		public void setUsuarioNome(String usuario) {
			this.usuarioNome = usuario;
		}

		public String getCentroCusto() {
			return centroCusto;
		}

		public void setCentroCusto(String centroCusto) {
			this.centroCusto = centroCusto;
		}

		public String getRdv() {
			return rdv;
		}

		public void setRdv(String rdv) {
			this.rdv = rdv;
		}

		public double getDespesaTotal() {
			return despesaTotal;
		}

		public void setDespesaTotal(double despesaTotal) {
			this.despesaTotal = despesaTotal;
		}

		public double getDespesaDinheiro() {
			return despesaDinheiro;
		}

		public void setDespesaDinheiro(double despesaDinheiro) {
			this.despesaDinheiro = despesaDinheiro;
		}

		public double getDespesaKM() {
			return despesaKM;
		}

		public void setDespesaKM(double despesaKM) {
			this.despesaKM = despesaKM;
		}

		public double getKMReceber() {
			return KMReceber;
		}

		public void setKMReceber(double kMReceber) {
			KMReceber = kMReceber;
		}

		public double getValorReceber() {
			return valorReceber;
		}

		public void setValorReceber(double valorReceber) {
			this.valorReceber = valorReceber;
		}
	}

}
