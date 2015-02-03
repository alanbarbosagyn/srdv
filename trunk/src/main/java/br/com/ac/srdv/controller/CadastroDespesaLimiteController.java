package br.com.ac.srdv.controller;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.RowEditEvent;

import br.com.ac.srdv.dao.DespesaLimiteDao;
import br.com.ac.srdv.dao.CicloDao;
import br.com.ac.srdv.dao.UsuarioDao;
import br.com.ac.srdv.modelo.DespesaLimite;
import br.com.ac.srdv.modelo.Ciclo;
import br.com.ac.srdv.modelo.Usuario;
import br.com.ac.srdv.util.Mensagem;

public class CadastroDespesaLimiteController {

	private List<Ciclo> listaRdv;
	private Ciclo rdv;
	private List<WrapperA> lista;

	public CadastroDespesaLimiteController() {
		novo();
	}

	public String novo() {
		rdv = new Ciclo();
		rdv.setLiberado(true);
		lista = new ArrayList<WrapperA>();

		try {
			listaRdv = new CicloDao().getTodosTipos();
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CONSULTAR RDV", e.getMessage());
		}

		return "CadastroDespesaLimite";
	}

	public void onRowEdit(RowEditEvent event) {

		// SALVA NO BANCO DE DADOS O REGISTRO ALTERADO
		WrapperA a = (WrapperA) event.getObject();

		DespesaLimite desp = new DespesaLimite();
		desp.setUsuario(a.getLogin());
		desp.setTipo(a.getTipo());
		desp.setLimite(a.getLimiteAtual());

		try {
			new DespesaLimiteDao().salvar(desp);
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO SALVAR ALTERA��O NO LIMITE DE DESPESAS!",
					e.getMessage());
			e.printStackTrace();
			return;
		}

		Mensagem.Info("LIMITE DE DESPESA ALTERADO!", "");

	}

	public void onRowCancel(RowEditEvent event) {
		// FacesMessage msg = new FacesMessage("Edit Cancelled",
		// ((Agenda) event.getObject()).getData() + "");
		// FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String consultar() {

		DespesaLimiteDao despDao = new DespesaLimiteDao();
		lista = new ArrayList<WrapperA>();
		List<DespesaLimite> laux;
		try {
			laux = despDao.getLimites(rdv.getId());
		} catch (Exception e) {
			Mensagem.Erro("ERRO AO CARREGAR LIMITES", e.getMessage());
			e.printStackTrace();
			return "";
		}

		UsuarioDao userDao = new UsuarioDao();
		lista = new ArrayList<CadastroDespesaLimiteController.WrapperA>();
		for (DespesaLimite d : laux) {

			WrapperA w = new WrapperA();
			lista.add(w);

			w.setLimiteAtual(d.getLimite());
			w.setTipo(rdv.getId());
			w.setLimiteOriginal(d.getLimiteOriginal());
			w.setLogin(d.getUsuario());

			try {
				Usuario u = userDao.getUsuario(d.getUsuario());
				w.setNome(u.getNome());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	public List<Ciclo> getListaRdv() {
		return listaRdv;
	}

	public void setListaRdv(List<Ciclo> listaRdv) {
		this.listaRdv = listaRdv;
	}

	public Ciclo getRdv() {
		return rdv;
	}

	public void setRdv(Ciclo rdv) {
		this.rdv = rdv;
	}

	public List<WrapperA> getLista() {
		return lista;
	}

	public void setLista(List<WrapperA> lista) {
		this.lista = lista;
	}

	public class WrapperA {
		private String login;
		private String nome;
		private int tipo;
		private double limiteOriginal;
		private double limiteAtual;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public double getLimiteOriginal() {
			return limiteOriginal;
		}

		public void setLimiteOriginal(double limiteOriginal) {
			this.limiteOriginal = limiteOriginal;
		}

		public double getLimiteAtual() {
			return limiteAtual;
		}

		public void setLimiteAtual(double limiteAtual) {
			this.limiteAtual = limiteAtual;
		}

		public int getTipo() {
			return tipo;
		}

		public void setTipo(int tipo) {
			this.tipo = tipo;
		}
	}
}
